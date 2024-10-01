package org.gameserver.configuration;

import lombok.RequiredArgsConstructor;
import org.gameserver.controllers.GameWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer{//WebSocketMessageBrokerConfigurer {
    private final GameWebSocketHandler gameWebSocketHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler, "/websocket").setAllowedOrigins("*");
    }
}
