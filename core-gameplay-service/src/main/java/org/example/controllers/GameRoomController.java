package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.EnterRoomRequestDto;
import org.example.dto.EnterRoomResponseDto;
import org.example.enums.ResponseCode;
import org.example.services.GameRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gameRoom")
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping("/create")
    public ResponseEntity<String> createRoom(){
        var room = gameRoomService.createRoom();
        return ResponseEntity.ok(room.getId());
    }

    @PostMapping("/join")
    private ResponseEntity<EnterRoomResponseDto> joinRoom(@RequestBody EnterRoomRequestDto enterRoomRequestDto) {
        var success = gameRoomService.joinRoom(enterRoomRequestDto.getRoomId(), enterRoomRequestDto.getPlayerId());
        if (success) {
            return ResponseEntity.ok(new EnterRoomResponseDto(ResponseCode.OK));
        } else{
            return ResponseEntity.ok(new EnterRoomResponseDto(ResponseCode.ROOM_NOT_FOUND));
        }
    }

    @GetMapping()
    public ResponseEntity check() {
        return ResponseEntity.ok().build();
    }
}
