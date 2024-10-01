package org.gameserver.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.gameserver.handlers.IEventHandler;
import org.gameserver.handlers.MoveEventHandler;
import org.gameserver.handlers.ShootEventHandler;
import org.gameserver.handlers.StartGameHandler;
import org.gameserver.models.events.EventSender;
import org.gameserver.models.events.GameEvent;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class Room {
    @Getter
    private final String id;
    @Getter
    private RoomState state;
    private final List<Player> players;
    private final ObjectMapper jsonMapper;
    private final Logger logger = Logger.getLogger(Room.class.getName());
    private final BlockingQueue<EventSender> eventQueue = new LinkedBlockingQueue<>();
    private final List<IEventHandler> eventHandlers = new ArrayList<>();
    private final StartGameHandler startGameHandler;


    public Room(String id, ObjectMapper jsonMapper,
                float[] firstSpawnPoint, float[] secondSpawnPoint) {
        this.id = id;
        players = new CopyOnWriteArrayList<>();
        state = RoomState.WAITING;
        this.jsonMapper = jsonMapper;
        startGameHandler = new StartGameHandler(firstSpawnPoint, secondSpawnPoint, this::handleEvent, this::start);
        eventHandlers.add(new MoveEventHandler(this::handleEvent, this::handleEventOther, this::getPlayerBySession));
        eventHandlers.add(new ShootEventHandler(this::handleEvent, this::getPlayerBySession, this::getOtherPlayer, this :: endGame));
        eventHandlers.add(startGameHandler);
    }

    public void addPlayer(Player player) {
        if (state == RoomState.WAITING) {
            players.add(player);
            startGameHandler.addPlayer(player);
        }
    }

    public void startGame() {
        if (players.size() < 2) {
            return;
        }
        state = RoomState.IN_PROGRESS;
        startGameHandler.spawnPlayers();
    }

    private void start(){
        broadcast("start_game", "Game started");
    }

    public void addEvent(GameEvent event, WebSocketSession session) {
        if (state != RoomState.IN_PROGRESS) {
            return;
        }
        Player player = getPlayerBySession(session);
        if (player == null) {
            return;
        }
        event.setPlayerId(player.getId());
        eventQueue.add(new EventSender(session, event));
    }

    public void processEvent() {
        EventSender event;

        while ((event = eventQueue.poll()) != null) {
            for(var handler : eventHandlers) {
                handler.handle(event);
            }
        }
    }

    private void handleEventOther(GameEvent event, WebSocketSession session) {
        try {
            broadcast("game_command", jsonMapper.writeValueAsString(event), session);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPlayersCount() {
        return players.size();
    }

    private void handleEvent(GameEvent event) {
        try {
            broadcast("game_command", jsonMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void endGame(Player player){
        handleEvent(GameEvent.builder().cmd("game_over").playerId(player.getId()).build());
        state = RoomState.CLOSED;
    }

    private void broadcast(String command, String message, WebSocketSession session) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(command, id, message);
        try {
            String jsonMessage = jsonMapper.writeValueAsString(webSocketMessage);
            for (var player : players) {
                if (player.getSession().equals(session)) {
                    continue;
                }
                player.getSession().sendMessage(new TextMessage(jsonMessage));
            }
        } catch (Exception e) {
            logger.severe("Error broadcasting message: " + e.getMessage());
        }
    }

    private void broadcast(String command, String message) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(command, id, message);
        try {
            String jsonMessage = jsonMapper.writeValueAsString(webSocketMessage);
            for (var player : players) {
                player.getSession().sendMessage(new TextMessage(jsonMessage));
            }
        } catch (Exception e) {
            logger.severe("Error broadcasting message: " + e.getMessage());
        }
    }

    public boolean removePlayer(WebSocketSession session) {
        Player player = getPlayerBySession(session);
        if (player == null) {
            return false;
        }
        players.remove(player);
        broadcast("player_left", player.getId());
        if (getPlayersCount() == 0) {
            state = RoomState.CLOSED;
        }
        return true;
    }

    private Player getPlayerBySession(WebSocketSession session) {
        return players.stream()
                .filter(p -> p.getSession().equals(session))
                .findFirst()
                .orElse(null);
    }

    private Player getOtherPlayer(Player player) {
        return players.stream()
                .filter(p -> !p.equals(player))
                .findFirst()
                .orElse(null);
    }
}
