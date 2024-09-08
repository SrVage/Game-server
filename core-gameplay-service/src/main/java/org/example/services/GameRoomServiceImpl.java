package org.example.services;

import org.example.models.Player;
import org.example.models.Room;
import org.example.models.RoomState;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GameRoomServiceImpl implements GameRoomService {
    private final ExecutorService roomThreadPool = Executors.newCachedThreadPool();
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Room createRoom() {
        String uuid = UUID.randomUUID().toString();
        Room room = new Room(uuid);

        roomThreadPool.submit(() -> runRoom(room));

        rooms.put(uuid, room);
        return room;
    }

    @Override
    public boolean joinRoom(String roomId, String playerId){
        Room room = rooms.get(roomId);
        if(room == null){
            return false;
        }
        var player = new Player(playerId);
        room.addPlayer(player);
        return true;
    }

    @Override
    public Room getRoom(String roomId){
        return rooms.get(roomId);
    }

    private void runRoom(Room room) {
        while (room.getState() != RoomState.CLOSED){
            room.processEvent();
        }
    }
}
