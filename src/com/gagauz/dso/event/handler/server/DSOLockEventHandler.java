package com.gagauz.dso.event.handler.server;

import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.DSOLockEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.thread.DSOServerThread;

import java.util.logging.Logger;

public class DSOLockEventHandler implements DSOEventHandler {

    private static final Logger log = Logger.getLogger("DSOLockEventHandler");

    private DSOServerThread dsoServerThread;

    public DSOLockEventHandler(DSOServerThread dsoServerThread) {
        this.dsoServerThread = dsoServerThread;
    }

    @Override
    public void handleEvent(DSOEvent event) {
        log.info("** Handle lock event");
        DSOLockEvent lockEvent = (DSOLockEvent) event;
        lockEvent.setNodeId(dsoServerThread.hashCode());
        dsoServerThread.getDsoServer().lock(lockEvent);
    }
}
