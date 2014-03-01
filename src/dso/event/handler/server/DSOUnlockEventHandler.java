package dso.event.handler.server;

import java.util.logging.Logger;

import dso.event.DSOEvent;
import dso.event.DSOUnlockEvent;
import dso.thread.DSOServer;
import dso.thread.DSOServerThread;

public class DSOUnlockEventHandler implements DSOEventHandler {

    private static final Logger log = Logger.getLogger("DSOLockEventHandler");

    private final DSOServer dsoServer;
    private final DSOServerThread dsoServerThread;

    public DSOUnlockEventHandler(DSOServer dsoServer, DSOServerThread dsoServerThread) {
        this.dsoServer = dsoServer;
        this.dsoServerThread = dsoServerThread;
    }

    @Override
    public void handleEvent(DSOEvent event) {
        log.info("** Handle lock event");
        DSOUnlockEvent lockEvent = (DSOUnlockEvent) event;
        dsoServer.unlock(lockEvent);
    }
}
