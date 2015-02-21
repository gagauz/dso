package com.gagauz.dso.event.handler.server;

import com.gagauz.dso.DSO;
import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.DSOShareObjectEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.thread.DSOServerThread;

import java.util.logging.Logger;

public class DSOShareObjectEventHandler implements DSOEventHandler {

    private static final Logger log = Logger.getLogger("DSOShareObjectEventHandler");

    private DSOServerThread dsoServerThread;

    public DSOShareObjectEventHandler(DSOServerThread dsoServerThread) {
        this.dsoServerThread = dsoServerThread;
    }

    @Override
    public void handleEvent(DSOEvent event) {
        log.info("*** handle share");
        DSO.updateLocal(((DSOShareObjectEvent) event).getDelta());
        dsoServerThread.getDsoServer().propagate(dsoServerThread, event);
    }
}
