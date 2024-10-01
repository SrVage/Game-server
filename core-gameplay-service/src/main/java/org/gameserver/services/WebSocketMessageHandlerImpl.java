package org.gameserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.gameserver.models.WebSocketMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class WebSocketMessageHandlerImpl implements WebSocketMessageHandler{
    private final ObjectMapper jsonMapper;
    private final Logger logger = Logger.getLogger(WebSocketMessageHandlerImpl.class.getName());
    private final GameRoomService gameRoomService;

    @Override
    public void handleMessage(WebSocketSession session, String message) {
        try {
            var wsMessage = jsonMapper.readValue(message, WebSocketMessage.class);
            switch (wsMessage.getCommand()){
                case "room":
                    gameRoomService.joinRoom(wsMessage.getRoomId(), wsMessage.getMessage(), session);
                    break;
                case "game_command":
                    gameRoomService.addEvent(wsMessage.getRoomId(), wsMessage.getMessage(), session);
                    break;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void playerLeft(WebSocketSession session) {
        gameRoomService.removePlayer(session);
    }
}
