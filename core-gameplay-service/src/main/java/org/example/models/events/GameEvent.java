package org.example.models.events;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public abstract class GameEvent {
    private String playerId;
    private float positionX;
    private float positionY;
    private float rotateAngle;
}
