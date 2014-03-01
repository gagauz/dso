package dso.event.handler.server;

import dso.event.DSOEvent;
import dso.event.DSOLockEvent;
import dso.thread.DSOServer;
import dso.thread.DSOServerThread;

import java.util.logging.Logger;

public class DSOLockEventHandler implements DSOEventHandler {

    private static final Logger log = Logger.getLogger("DSOLockEventHandler");

    private DSOServer dsoServer;
    private DSOServerThread dsoServerThread;

    public DSOLockEventHandler(DSOServer dsoServer, DSOServerThread dsoServerThread) {
        this.dsoServer = dsoServer;
        this.dsoServerThread = dsoServerThread;
    }

    @Override
    public void handleEvent(DSOEvent event) {
        log.info("** Handle lock event");
        DSOLockEvent lockEvent = (DSOLockEvent) event;
        lockEvent.setNodeId(dsoServerThread.hashCode());
        dsoServer.lock(lockEvent);
    }
}
