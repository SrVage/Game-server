package org.gameserver.dto;

import lombok.Getter;
import org.gameserver.enums.ResponseCode;

@Getter
public class EnterRoomResponseDto {
    private final ResponseCode code;
    private final String message;
    private final String roomId;

    public EnterRoomResponseDto(ResponseCode code, String roomId) {
        this.code = code;
        this.message = code.toString();
        this.roomId = roomId;
    }
}
