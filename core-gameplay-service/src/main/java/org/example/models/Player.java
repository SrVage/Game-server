package org.example.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Getter
public class Player {
    private final String id;
    @Getter
    private float positionX;
    @Getter
    private float positionY;
    @Getter
    private float rotation;
    private final WebSocketSession session;
    private Timestamp lastPositionUpdate = new Timestamp(System.currentTimeMillis());
    @Getter
    private int hp;
    private final float speed;

    public boolean updatePosition(float positionX, float positionY, float rotation) {
        if (checkDistance(this.positionX, this.positionY, positionX, positionY) > speed * getTimeDifferenceInSeconds(new Timestamp(System.currentTimeMillis()), lastPositionUpdate)) {
            return false;
        }
        this.positionX = positionX;
        this.positionY = positionY;
        this.rotation = rotation;
        this.lastPositionUpdate = new Timestamp(System.currentTimeMillis());
        return true;
    }

    public void Damage(int damage) {
        this.hp -= damage;
    }

    private static float checkDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private static float getTimeDifferenceInSeconds(Timestamp t1, Timestamp t2) {
        return (float) (t1.getTime() - t2.getTime()) / 1000;
    }
}
