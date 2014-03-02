package dso.event.api;

import dso.event.DSOEvent;

public interface DSOEventHandler {
    void handleEvent(DSOEvent event);
}
