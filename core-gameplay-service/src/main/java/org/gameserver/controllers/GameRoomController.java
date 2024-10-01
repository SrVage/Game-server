package org.gameserver.controllers;

import lombok.RequiredArgsConstructor;
import org.gameserver.dto.EnterRoomResponseDto;
import org.gameserver.enums.ResponseCode;
import org.gameserver.models.Room;
import org.gameserver.services.GameRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gameRoom")
public class GameRoomController {
    private final GameRoomService gameRoomService;
    private final Logger logger = Logger.getLogger(GameRoomController.class.getName());

    @PostMapping("/connect")
    public ResponseEntity<EnterRoomResponseDto> connect(@RequestHeader String token) {
        Room room = gameRoomService.getFreeRoom();
        if (room == null) {
            room = gameRoomService.createRoom();
        }
        return ResponseEntity.ok(new EnterRoomResponseDto(ResponseCode.OK, room.getId()));
    }

    @GetMapping()
    public ResponseEntity<String> check() {
        String hostAddress = "";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
            logger.info("Host Address: " + hostAddress);
        } catch (UnknownHostException e) {
            logger.severe("Unable to get Host Address: " + e.getMessage());
        }
        return ResponseEntity.ok(hostAddress);
    }
}
