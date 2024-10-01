package org.gameserver.services;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketMessageHandler {
    void handleMessage(WebSocketSession id, String message);

    void playerLeft(WebSocketSession session);
}
