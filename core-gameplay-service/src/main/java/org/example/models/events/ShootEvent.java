package org.example.models.events;

import org.example.models.Position;

public class ShootEvent extends GameEvent{
    public ShootEvent(String playerId, Position position, float rotateAngle) {
        super(playerId, position, rotateAngle);
    }
}
