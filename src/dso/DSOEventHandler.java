package dso;

import dso.event.DSOEvent;

public interface DSOEventHandler {
    void handle(DSOEvent event);

    void handleDisconnect();
}
