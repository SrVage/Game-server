package org.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDto {
    private boolean success;
    private UUID uuid;
}
