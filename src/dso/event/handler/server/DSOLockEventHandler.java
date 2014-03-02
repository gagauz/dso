package dso.event.handler.server;

import dso.event.DSOEvent;
import dso.event.DSOLockEvent;
import dso.event.api.DSOEventHandler;
import dso.thread.DSOServerThread;

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
