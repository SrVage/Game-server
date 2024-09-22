package org.example.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.example.models.events.GameEvent;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Room {
    @Getter
    private final String id;
    @Getter
    private RoomState state;
    private final List<Player> players;
    private final ObjectMapper jsonMapper;

    private final BlockingQueue<GameEvent> eventQueue = new LinkedBlockingQueue<>();


    public Room(String id, ObjectMapper jsonMapper) {
        this.id = id;
        players = new ArrayList<>();
        state = RoomState.WAITING;
        this.jsonMapper = jsonMapper;
    }

    public void addPlayer(Player player) {
        if (state == RoomState.WAITING) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
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

    public void addEvent(GameEvent event) {
        eventQueue.add(event);
    }

    public void processEvent() {
        GameEvent event;
        while ((event = eventQueue.poll()) != null) {
            handleEvent(event);
        }
    }

    public int getPlayersCount() {
        return players.size();
    }

    private void handleEvent(GameEvent event) {

    }

    private void broadcast(String comand, String message) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(comand, id, message);
        try {
            String jsonMessage = jsonMapper.writeValueAsString(webSocketMessage);
            for (var player : players) {
                player.getSession().sendMessage(new TextMessage(jsonMessage));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removePlayer(WebSocketSession session) {
        Player player = players.stream()
                .filter(p -> p.getSession().equals(session))
                .findFirst()
                .orElse(null);
        if (player == null) {
            return false;
        }
        players.remove(player);
            broadcast("player_left", player.getId());
        return true;
    }
}
