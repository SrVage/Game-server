package org.gameserver.handlers;

import org.gameserver.models.Player;
import org.gameserver.models.Vector;
import org.gameserver.models.events.EventSender;
import org.gameserver.models.events.GameEvent;
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
    private final Function<WebSocketSession, Player> getPlayerBySession;
    private final Function<Player, Player> getOtherPlayer;
    private final Consumer<Player> endGame;
    private final String command = "shoot";

    public ShootEventHandler(Consumer<GameEvent> allPlayerHandler,
                             Function<WebSocketSession, Player> getPlayerBySession,
                             Function<Player, Player> getOtherPlayer, Consumer<Player> endGame) {
        this.allPlayerHandler = allPlayerHandler;
        this.getPlayerBySession = getPlayerBySession;
        this.getOtherPlayer = getOtherPlayer;
        this.endGame = endGame;
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
        float[] vectorBetween = Vector.createVector(new float[]{secondPlayer.getPositionX(), secondPlayer.getPositionY()}, new float[]{player.getPositionX(), player.getPositionY()});
        float[] vectorBetweenNormalize = Vector.normalizeVector(vectorBetween);
        float dotProduct = Vector.dotProduct(vectorBetweenNormalize, Vector.createUnitVector(player.getRotation()));
        float distance = Vector.calculateDistance(vectorBetween);

        float distanceFactor = Math.min(Math.max((distance - minDistance) / (maxDistance - minDistance), 0.0f), 1.0f);
        float adjustedDotProductThreshold = minDotProductThreshold + distanceFactor * (maxDotProductThreshold - minDotProductThreshold);

        if (dotProduct >= adjustedDotProductThreshold) {
            if (secondPlayer.damage(player.getDamage())) {
                var newEvent = GameEvent.builder().cmd("death")
                        .playerId(secondPlayer.getId())
                        .build();
                allPlayerHandler.accept(newEvent);
                endGame.accept(player);
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
}
