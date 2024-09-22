package org.example.services;

import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.example.dtos.GameServerUrlResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService{
    @Value("${CORE_GAMEPLAY_SERVICE_URL}")
    private String coreGameplayServiceUrl;
    @Value("${CORE_GAMEPLAY_SERVICE_PORT}")
    private String coreGameplayServicePort;

    private final CloseableHttpClient restClient;
    private final String api = "/api/v1/gameRoom";
    private final Logger logger = Logger.getLogger(IntegrationServiceImpl.class.getName());

    @Override
    public GameServerUrlResponseDto isCoreGameplayServiceAvailable() {
        GameServerUrlResponseDto gameServerUrlResponseDto = new GameServerUrlResponseDto();
        HttpGet request = new HttpGet(coreGameplayServiceUrl+":"+coreGameplayServicePort+api);
        try (CloseableHttpResponse response = restClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.warning(responseBody);
            gameServerUrlResponseDto.setUrl(responseBody+":"+coreGameplayServicePort + api);
            gameServerUrlResponseDto.setAvailable(response.getCode() == 200);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            gameServerUrlResponseDto.setAvailable(false);
        }
        logger.warning(gameServerUrlResponseDto.getUrl());
        return gameServerUrlResponseDto;
    }
}
