package org.gameserver.services;

import org.gameserver.dtos.GameServerUrlResponseDto;

public interface IntegrationService {
    GameServerUrlResponseDto isCoreGameplayServiceAvailable();
}
