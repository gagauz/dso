package com.gagauz.dso.event.handler.server;

import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.DSOUnlockEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.thread.DSOServerThread;

import java.util.logging.Logger;

public class DSOUnlockEventHandler implements DSOEventHandler {

    private static final Logger log = Logger.getLogger("DSOLockEventHandler");

    private final DSOServerThread dsoServerThread;

    public DSOUnlockEventHandler(DSOServerThread dsoServerThread) {
        this.dsoServerThread = dsoServerThread;
    }

    @Override
    public void handleEvent(DSOEvent event) {
        log.info("** Handle lock event");
        DSOUnlockEvent lockEvent = (DSOUnlockEvent) event;
        dsoServerThread.getDsoServer().unlock(lockEvent);
    }
}
