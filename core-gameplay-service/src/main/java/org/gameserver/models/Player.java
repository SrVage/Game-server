package org.gameserver.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Getter
public class Player {
    private final String id;
    private float positionX;
    private float positionY;
    private float rotation;
    private final WebSocketSession session;
    private Timestamp lastPositionUpdate = new Timestamp(System.currentTimeMillis());
    private int hp = 100;
    private final float speed;
    private final int damage;
    private boolean isDead = false;

    public Player(String id, WebSocketSession session, float speed, int health, int damage) {
        this.id = id;
        this.session = session;
        this.speed = speed;
        this.hp = health;
        this.damage = damage;
    }

    public void setStartPosition(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public boolean updatePosition(float positionX, float positionY, float rotation) {
        if (isDead){
            return false;
        }
        if (checkDistance(this.positionX, this.positionY, positionX, positionY) > speed * getTimeDifferenceInSeconds(new Timestamp(System.currentTimeMillis()), lastPositionUpdate)) {
            return false;
        }
        this.positionX = positionX;
        this.positionY = positionY;
        this.rotation = rotation;
        this.lastPositionUpdate = new Timestamp(System.currentTimeMillis());
        return true;
    }

    public boolean damage(int damage) {
        if (isDead){
            return true;
        }

        this.hp -= damage;
        if (hp<=0){
            isDead = true;
        }
        return this.hp <= 0;
    }

    private static float checkDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private static float getTimeDifferenceInSeconds(Timestamp t1, Timestamp t2) {
        return (float) (t1.getTime() - t2.getTime()) / 1000;
    }
}
