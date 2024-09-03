package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dtos.LoginRequestDto;
import org.example.dtos.LoginResponseDto;
import org.example.services.AuthorizationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authorization")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return authorizationService.login(loginRequestDto);
    }

    @PostMapping("/register")
    public LoginResponseDto register(@RequestBody LoginRequestDto loginRequestDto) {
        return authorizationService.register(loginRequestDto);
    }
}