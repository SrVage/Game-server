package org.example.dto;

import lombok.Getter;
import org.example.enums.ResponseCode;

@Getter
public class EnterRoomResponseDto {
    private final ResponseCode code;
    private final String message;

    public EnterRoomResponseDto(ResponseCode code) {
        this.code = code;
        this.message = code.toString();
    }
}
