package org.example.models.events;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventSender {
    private final WebSocketSession session;
    private final GameEvent event;
    private boolean all = false;
}
