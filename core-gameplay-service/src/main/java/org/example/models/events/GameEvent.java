package org.example.models.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class GameEvent {
    private String playerId;
    private String cmd;
    private Float positionX;
    private Float positionY;
    private Float rotateAngle;
    private Float secondPlayerPositionX;
    private Float secondPlayerPositionY;
    private Float secondPlayerRotateAngle;
    private Integer playerHp;
}
