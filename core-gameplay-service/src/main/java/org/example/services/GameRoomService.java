package org.example.services;

import org.example.models.Room;
import org.springframework.web.socket.WebSocketSession;

public interface GameRoomService {
    Room createRoom();

    boolean joinRoom(String roomId, String message, WebSocketSession playerId);

    Room getRoom(String roomId);

    Room getFreeRoom();

    void removePlayer(WebSocketSession session);
}
