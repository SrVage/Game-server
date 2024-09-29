package org.example.factories;

import lombok.RequiredArgsConstructor;
import org.example.configuration.PlayerProperties;
import org.example.models.Player;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
public class PlayerFactory implements IFactory<Player, WebSocketSession>{
    private final PlayerProperties playerProperties;

    @Override
    public Player create(String id, WebSocketSession session) {
        return new Player(id, session, playerProperties.getSpeed(),
                playerProperties.getInitialHealth(), playerProperties.getDamage());
    }
}
