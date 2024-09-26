package org.example.handlers;

import org.example.models.Player;
import org.example.models.events.EventSender;
import org.example.models.events.GameEvent;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class MoveEventHandler implements IEventHandler{
    private final Consumer<GameEvent> allPlayerHandler;
    private final BiConsumer<GameEvent, WebSocketSession> otherPlayerHandler;
    private final Function<WebSocketSession, Player> getPlayerBySession;
    private final String command = "move";

    public MoveEventHandler(Consumer<GameEvent> allPlayerHandler,
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
        if (player.updatePosition(event.getEvent().getPositionX(), event.getEvent().getPositionY(), event.getEvent().getRotateAngle())) {
            otherPlayerHandler.accept(event.getEvent(), event.getSession());
        } else {
            var newEvent = new GameEvent(player.getId(), command, player.getPositionX(), player.getPositionY(), player.getRotation());
            allPlayerHandler.accept(newEvent);
        }
    }
}
