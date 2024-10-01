package org.gameserver.handlers;

import org.gameserver.models.Player;
import org.gameserver.models.events.EventSender;
import org.gameserver.models.events.GameEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class StartGameHandler implements IEventHandler{

    private final float[] firstSpawnPoint;
    private final float[] secondSpawnPoint;
    private Player firstPlayer;
    private Player secondPlayer;
    private final Consumer<GameEvent> allPlayerHandler;
    private final Runnable start;
    private volatile ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    private volatile ConcurrentHashMap<String, GameEvent> playerEvent = new ConcurrentHashMap<>();
    private final String command = "ready";

    public StartGameHandler(float[] firstSpawnPoint, float[] secondSpawnPoint,
                            Consumer<GameEvent> allPlayerHandler, Runnable start) {
        this.firstSpawnPoint = firstSpawnPoint;
        this.secondSpawnPoint = secondSpawnPoint;
        this.allPlayerHandler = allPlayerHandler;
        this.start = start;
    }

    public void addPlayer(Player player) {
        if (firstPlayer == null) {
            firstPlayer = player;
        } else {
            secondPlayer = player;
        }
    }

    public void spawnPlayers() {
        firstPlayer.setStartPosition(firstSpawnPoint[0], firstSpawnPoint[1]);
        secondPlayer.setStartPosition(secondSpawnPoint[0], secondSpawnPoint[1]);
        var firstPlayerEvent = GameEvent.builder()
                .cmd("spawn")
                .playerId(firstPlayer.getId())
                .positionX(firstPlayer.getPositionX())
                .positionY(firstPlayer.getPositionY())
                .rotateAngle(firstPlayer.getRotation())
                .build();
        var secondPlayerEvent = GameEvent.builder()
                .cmd("spawn")
                .playerId(secondPlayer.getId())
                .positionX(secondPlayer.getPositionX())
                .positionY(secondPlayer.getPositionY())
                .rotateAngle(secondPlayer.getRotation())
                .build();

        players.put(firstPlayer.getId(), firstPlayer);
        playerEvent.put(firstPlayer.getId(), firstPlayerEvent);
        players.put(secondPlayer.getId(), secondPlayer);
        playerEvent.put(secondPlayer.getId(), secondPlayerEvent);

        new Thread(() -> {
            try {
                while (!players.isEmpty()) {
                    for (var player : players.entrySet()){
                        allPlayerHandler.accept(playerEvent.get(player.getKey()));
                    }
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void handle(EventSender event) {
        if (!command.equals(event.getEvent().getCmd())) {
            return;
        }
        players.remove(event.getEvent().getPlayerId());
        if (players.isEmpty()){
            start.run();
            //allPlayerHandler.accept(GameEvent.builder().cmd("start_game").build());
        }
    }
}