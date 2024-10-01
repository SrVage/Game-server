package org.gameserver.services;

import org.gameserver.dtos.LoginRequestDto;
import org.gameserver.dtos.LoginResponseDto;

public interface AuthorizationService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    LoginResponseDto register(LoginRequestDto loginRequestDto);

    boolean checkToken(String token);
}
