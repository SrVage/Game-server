package org.example.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
@Getter
@Setter
public class Player {
    private final String id;
    private Position position;
    private float rotation;
    private final WebSocketSession session;
}
