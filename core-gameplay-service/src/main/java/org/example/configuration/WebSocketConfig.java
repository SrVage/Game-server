package org.example.configuration;

import lombok.RequiredArgsConstructor;
import org.example.controllers.GameWebSocketHandler;
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
