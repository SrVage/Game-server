package org.example.models.events;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GameEvent {
    private String playerId;
    private String cmd;
    private float positionX;
    private float positionY;
    private float rotateAngle;
}
