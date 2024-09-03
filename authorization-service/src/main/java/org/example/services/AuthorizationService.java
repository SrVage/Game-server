package org.example.services;

import org.example.dtos.LoginRequestDto;
import org.example.dtos.LoginResponseDto;

public interface AuthorizationService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    LoginResponseDto register(LoginRequestDto loginRequestDto);
}
