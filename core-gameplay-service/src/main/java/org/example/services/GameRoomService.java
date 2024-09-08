package org.example.services;

import org.example.models.Room;

public interface GameRoomService {
    Room createRoom();

    boolean joinRoom(String roomId, String playerId);

    Room getRoom(String roomId);
}
