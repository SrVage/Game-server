package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dtos.GameServerUrlResponseDto;
import org.example.dtos.LoginRequestDto;
import org.example.dtos.LoginResponseDto;
import org.example.errors.BadDataException;
import org.example.services.AuthorizationService;
import org.example.services.IntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authorization")
public class AuthorizationController {
    private final AuthorizationService authorizationService;
    private final IntegrationService integrationService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return authorizationService.login(loginRequestDto);
    }

    @PostMapping("/register")
    public LoginResponseDto register(@RequestBody LoginRequestDto loginRequestDto) {
        return authorizationService.register(loginRequestDto);
    }

    @GetMapping()
    public ResponseEntity check() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/game-server-url")
    public GameServerUrlResponseDto getGameServerUrl(@RequestHeader String token) {
        if (!authorizationService.checkToken(token)){
            throw new BadDataException("Invalid token");
        }
        return integrationService.isCoreGameplayServiceAvailable();
    }
}