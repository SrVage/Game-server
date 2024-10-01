package org.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameServerUrlResponseDto {
    private boolean available;
    private String url;
}
