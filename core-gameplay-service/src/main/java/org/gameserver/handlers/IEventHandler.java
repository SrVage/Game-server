package org.gameserver.handlers;

import org.gameserver.models.events.EventSender;

public interface IEventHandler {
    void handle(EventSender event);
}
