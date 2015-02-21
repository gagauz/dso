package com.gagauz.dso.event.handler.server;

import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.api.DSOEventHandler;
import com.gagauz.dso.thread.DSOServer;
import com.gagauz.dso.thread.DSOServerThread;


import java.util.logging.Logger;

public class DSONoopEventHandler implements DSOEventHandler {
    private static final Logger log = Logger.getLogger("DSONoopEventHandler");

    public DSONoopEventHandler() {
    }

    @Override
    public void handleEvent(DSOEvent event) {
        log.info("Handle NOOP");
    }

}
