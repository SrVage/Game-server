package org.example.models;

import lombok.Getter;
import org.example.models.events.GameEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Room {
    @Getter
    private final String id;
    @Getter
    private RoomState state;
    private final List<Player> players;

    private final BlockingQueue<GameEvent> eventQueue = new LinkedBlockingQueue<>();


    public Room(String id) {
        this.id = id;
        players = new ArrayList<>();
        state = RoomState.WAITING;
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

    private void handleEvent(GameEvent event) {

    }
}
