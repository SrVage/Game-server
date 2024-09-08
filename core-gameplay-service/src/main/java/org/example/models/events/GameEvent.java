package org.example.models.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.models.Position;

@RequiredArgsConstructor
@Getter
public abstract class GameEvent {
    private final String playerId;
    private final Position position;
    private final float rotateAngle;
}
