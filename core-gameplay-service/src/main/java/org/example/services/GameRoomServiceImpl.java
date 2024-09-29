package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.factories.IFactory;
import org.example.models.Player;
import org.example.models.Room;
import org.example.models.RoomState;
import org.example.models.events.GameEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {
    private final ExecutorService roomThreadPool = Executors.newCachedThreadPool();
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Logger logger = Logger.getLogger(GameRoomServiceImpl.class.getName());
    private final ObjectMapper jsonMapper;
    private final IFactory<Player, WebSocketSession> playerFactory;
    private final IFactory<Room, ObjectMapper> roomFactory;

    @Override
    public Room createRoom() {
        String uuid = UUID.randomUUID().toString();
        Room room = roomFactory.create(uuid, jsonMapper);

        roomThreadPool.submit(() -> runRoom(room));

        rooms.put(uuid, room);
        return room;
    }

    @Override
    public boolean joinRoom(String roomId, String message, WebSocketSession session){
        Room room = rooms.get(roomId);
        if(room == null){
            return false;
        }
        logger.info("Adding player to room " + roomId + " " + message);
        var player = playerFactory.create(message, session);
        room.addPlayer(player);
        if (room.getPlayersCount() == 2){
            room.startGame();
        }
        logger.warning("Player " + message + " joined room " + roomId);
        return true;
    }

    @Override
    public Room getRoom(String roomId){
        return rooms.get(roomId);
    }

    @Override
    public Room getFreeRoom(){
        return rooms.values().stream()
                .filter(room -> room.getState() == RoomState.WAITING)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removePlayer(WebSocketSession session) {
        rooms.values().forEach(room -> room.removePlayer(session));
    }

    @Override
    public void addEvent(String roomId, String message, WebSocketSession session) {
        Room room = rooms.get(roomId);
        if (room == null){
            return;
        }
        try {
            GameEvent gameEvent = jsonMapper.readValue(message, GameEvent.class);
            if (gameEvent != null) {
                room.addEvent(gameEvent, session);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void runRoom(Room room) {
        while (room.getState() != RoomState.CLOSED){
            room.processEvent();
        }
        logger.info("Room " + room.getId() + " closed");
        rooms.remove(room.getId());
    }
}
