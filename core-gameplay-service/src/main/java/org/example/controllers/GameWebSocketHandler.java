package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.WebSocketMessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class GameWebSocketHandler  extends TextWebSocketHandler {
    private final WebSocketMessageHandler webSocketMessageHandler;
    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private static final Map<String, WebSocketSession> rooms = Collections.synchronizedMap(new HashMap<>());
    private final Logger logger = Logger.getLogger(GameWebSocketHandler.class.getName());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("New connection opened: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        webSocketMessageHandler.handleMessage(session, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketMessageHandler.playerLeft(session);
    }

    private void broadcast(String message) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
