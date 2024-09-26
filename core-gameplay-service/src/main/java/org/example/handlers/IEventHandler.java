package org.example.handlers;

import org.example.models.events.EventSender;

public interface IEventHandler {
    void handle(EventSender event);
}
