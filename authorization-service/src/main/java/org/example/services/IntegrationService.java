package org.example.services;

import org.example.dtos.GameServerUrlResponseDto;

public interface IntegrationService {
    GameServerUrlResponseDto isCoreGameplayServiceAvailable();
}
