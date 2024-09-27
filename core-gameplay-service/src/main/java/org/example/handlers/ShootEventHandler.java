package org.example.handlers;

import org.example.models.Player;
import org.example.models.events.EventSender;
import org.example.models.events.GameEvent;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ShootEventHandler implements IEventHandler{
    private static final float minDistance = 0.0f;
    private static final float maxDistance = 4.0f;
    private static final float minDotProductThreshold = 0.5f;
    private static final float maxDotProductThreshold = 0.98f;
    private final Consumer<GameEvent> allPlayerHandler;
    private final BiConsumer<GameEvent, WebSocketSession> otherPlayerHandler;
    private final Function<WebSocketSession, Player> getPlayerBySession;
    private final Function<Player, Player> getOtherPlayer;
    private final String command = "shoot";

    public ShootEventHandler(Consumer<GameEvent> allPlayerHandler,
                             BiConsumer<GameEvent, WebSocketSession> otherPlayerHandler,
                             Function<WebSocketSession, Player> getPlayerBySession,
                             Function<Player, Player> getOtherPlayer) {
        this.allPlayerHandler = allPlayerHandler;
        this.otherPlayerHandler = otherPlayerHandler;
        this.getPlayerBySession = getPlayerBySession;
        this.getOtherPlayer = getOtherPlayer;
    }

    @Override
    public void handle(EventSender event){
        if (!command.equals(event.getEvent().getCmd())) {
            return;
        }
        var player = getPlayerBySession.apply(event.getSession());
        player.updatePosition(event.getEvent().getPositionX(), event.getEvent().getPositionY(), event.getEvent().getRotateAngle());
        var secondPlayer = getOtherPlayer.apply(player);
        var newEvent = GameEvent.builder()
                .cmd(command)
                .playerId(player.getId())
                .positionX(event.getEvent().getPositionX())
                .positionY(event.getEvent().getPositionY())
                .secondPlayerPositionX(secondPlayer.getPositionX())
                .secondPlayerPositionY(secondPlayer.getPositionY())
                .rotateAngle(event.getEvent().getRotateAngle())
                .secondPlayerRotateAngle(secondPlayer.getRotation()).build();
        allPlayerHandler.accept(newEvent);
        checkDamage(player, secondPlayer);
    }

    private void checkDamage(Player player, Player secondPlayer) {
        float[] vectorBetween = createVector(new float[]{secondPlayer.getPositionX(), secondPlayer.getPositionY()}, new float[]{player.getPositionX(), player.getPositionY()});
        float[] vectorBetweenNormalize = normalizeVector(vectorBetween);
        float dotProduct = dotProduct(vectorBetweenNormalize, createUnitVector(player.getRotation()));
        float distance = calculateDistance(vectorBetween);

        float distanceFactor = Math.min(Math.max((distance - minDistance) / (maxDistance - minDistance), 0.0f), 1.0f);
        float adjustedDotProductThreshold = minDotProductThreshold + distanceFactor * (maxDotProductThreshold - minDotProductThreshold);

        if (dotProduct >= adjustedDotProductThreshold) {
            if (secondPlayer.damage(10)) {
                var newEvent = GameEvent.builder().cmd("death")
                        .playerId(secondPlayer.getId())
                        .build();
                allPlayerHandler.accept(newEvent);
            }
            else{
                var newEvent = GameEvent.builder().cmd("damage")
                        .playerId(secondPlayer.getId())
                        .playerHp(secondPlayer.getHp())
                        .build();
                allPlayerHandler.accept(newEvent);
            }
        }
    }

    public static float[] createVector(float[] pointA, float[] pointB) {
        float[] vector = new float[pointA.length];
        for (int i = 0; i < pointA.length; i++) {
            vector[i] = pointB[i] - pointA[i];
        }
        return vector;
    }

    public static float[] createUnitVector(float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees-90);
        float x = (float) Math.cos(angleRadians);
        float y = (float) Math.sin(angleRadians);
        return new float[]{x, y};
    }

    public static float dotProduct(float[] vectorA, float[] vectorB) {
        float result = 0.0f;
        for (int i = 0; i < vectorA.length; i++) {
            result += vectorA[i] * vectorB[i];
        }
        return result;
    }

    public static float[] normalizeVector(float[] vector) {
        float magnitude = 0.0f;
        for (float v : vector) {
            magnitude += v * v;
        }
        magnitude = (float) Math.sqrt(magnitude);

        float[] normalizedVector = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = vector[i] / magnitude;
        }
        return normalizedVector;
    }

    private static float calculateDistance(float[] vector) {
        float sum = 0.0f;
        for (float v : vector) {
            sum += v * v;
        }
        return (float) Math.sqrt(sum);
    }
}
