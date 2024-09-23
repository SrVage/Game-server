package org.example.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.example.models.events.EventSender;
import org.example.models.events.GameEvent;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

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


    public Room(String id, ObjectMapper jsonMapper) {
        this.id = id;
        players = new CopyOnWriteArrayList<>();
        state = RoomState.WAITING;
        this.jsonMapper = jsonMapper;
    }

    public void addPlayer(Player player) {
        if (state == RoomState.WAITING) {
            players.add(player);
        }
    }

    public boolean startGame(){
        if (players.size() < 2){
            return false;
        }
        state = RoomState.IN_PROGRESS;
        broadcast("start_game", "Game started");
        return true;
    }

    public void completeGame(){
        state = RoomState.CLOSED;
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
            if (event.isAll())
            {
                handleEvent(event.getEvent());
            } else{
                handleEventOther(event.getEvent(), event.getSession());
            }
        }
    }

    private void handleEventOther(GameEvent event, WebSocketSession session) {
        try{
            broadcast("game_command", jsonMapper.writeValueAsString(event), session);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPlayersCount() {
        return players.size();
    }

    private void handleEvent(GameEvent event) {

    }

    private void broadcast(String comand, String message, WebSocketSession session) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(comand, id, message);
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

    private void broadcast(String comand, String message) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(comand, id, message);
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
        if (getPlayersCount() == 0){
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
}
