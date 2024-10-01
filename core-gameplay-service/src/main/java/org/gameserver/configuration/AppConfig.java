package org.gameserver.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gameserver.factories.IFactory;
import org.gameserver.factories.PlayerFactory;
import org.gameserver.factories.RoomFactory;
import org.gameserver.models.Player;
import org.gameserver.models.Room;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

@Configuration
@EnableConfigurationProperties({
        PlayerProperties.class,
        RoomProperties.class
})
public class AppConfig {

    @Bean
    public IFactory<Player, WebSocketSession> playerFactory(PlayerProperties properties) {
        return new PlayerFactory(properties);
    }

    @Bean
    public IFactory<Room, ObjectMapper> roomFactory(RoomProperties properties) {
        return new RoomFactory(properties);
    }
}
