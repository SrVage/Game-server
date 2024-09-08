package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.models.events.MoveEvent;
import org.example.models.events.ShootEvent;
import org.example.services.GameRoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {
    private final GameRoomService gameRoomService;

    @MessageMapping("/room/{roomId}/move")
    @SendTo("/room/{roomId}/topic/moves")
    public MoveEvent processMove(@DestinationVariable String roomId, MoveEvent event){
        var room = gameRoomService.getRoom(roomId);
        room.addEvent(event);
        return event;
    }

    @MessageMapping("/room/{roomId}/shoot")
    @SendTo("/room/{roomId}/topic/shoots")
    public ShootEvent processShoot(@DestinationVariable String roomId, ShootEvent event) {
        var room = gameRoomService.getRoom(roomId);
        room.addEvent(event);
        return event;
    }
}
