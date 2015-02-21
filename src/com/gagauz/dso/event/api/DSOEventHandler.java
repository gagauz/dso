package com.gagauz.dso.event.api;

import com.gagauz.dso.event.DSOEvent;

public interface DSOEventHandler {
    void handleEvent(DSOEvent event);
}
