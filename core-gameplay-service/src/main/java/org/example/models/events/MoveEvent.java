package org.example.models.events;

import org.example.models.Position;

public class MoveEvent extends GameEvent{
    public MoveEvent(String playerId, Position position, float rotateAngle) {
        super(playerId, position, rotateAngle);
    }
}
