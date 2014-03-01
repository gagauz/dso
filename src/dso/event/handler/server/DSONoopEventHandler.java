package dso.event.handler.server;

import dso.event.DSOEvent;
import dso.thread.DSOServer;
import dso.thread.DSOServerThread;

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
