package dso.event.api;

import dso.event.DSOEvent;

public interface DSOEventHandler<E extends DSOEvent<?>> {
    void handleEvent(E event);
}
