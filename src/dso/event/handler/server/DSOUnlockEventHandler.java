package dso.event.handler.server;

import dso.event.DSOEvent;
import dso.event.DSOUnlockEvent;
import dso.event.api.DSOEventHandler;
import dso.thread.DSOServerThread;

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
