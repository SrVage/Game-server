package org.example.handlers;

import org.example.models.Player;
import org.example.models.events.EventSender;
import org.example.models.events.GameEvent;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ShootEventHandler implements IEventHandler{

    private final Consumer<GameEvent> allPlayerHandler;
    private final BiConsumer<GameEvent, WebSocketSession> otherPlayerHandler;
    private final Function<WebSocketSession, Player> getPlayerBySession;
    private final String command = "shoot";

    public ShootEventHandler(Consumer<GameEvent> allPlayerHandler,
                             BiConsumer<GameEvent, WebSocketSession> otherPlayerHandler, Function<WebSocketSession, Player> getPlayerBySession) {
        this.allPlayerHandler = allPlayerHandler;
        this.otherPlayerHandler = otherPlayerHandler;
        this.getPlayerBySession = getPlayerBySession;
    }

    @Override
    public void handle(EventSender event){
        if (!command.equals(event.getEvent().getCmd())) {
            return;
        }
        var player = getPlayerBySession.apply(event.getSession());
        player.updatePosition(event.getEvent().getPositionX(), event.getEvent().getPositionY(), event.getEvent().getRotateAngle());
        var newEvent = new GameEvent(player.getId(), command, player.getPositionX(), player.getPositionY(), player.getRotation());
        allPlayerHandler.accept(newEvent);

    }

    public static float[] createUnitVector(float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees);
        float x = (float) Math.cos(angleRadians);
        float y = (float) Math.sin(angleRadians);
        return new float[]{x, y};
    }

    public static float dotProduct(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }

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

        if (magnitude == 0) {
            throw new IllegalArgumentException("Cannot normalize a zero vector");
        }

        float[] normalizedVector = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = vector[i] / magnitude;
        }
        return normalizedVector;
    }
}
