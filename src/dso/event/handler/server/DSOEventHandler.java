package dso.event.handler.server;

import dso.event.DSOEvent;

public interface DSOEventHandler {
    void handleEvent(DSOEvent event);
}
