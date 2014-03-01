package dso.event.handler.server;

import dso.DSO;
import dso.event.DSOEvent;
import dso.event.DSOShareObjectEvent;
import dso.thread.DSOServer;
import dso.thread.DSOServerThread;

import java.util.logging.Logger;

public class DSOShareObjectEventHandler implements DSOEventHandler {

    private static final Logger log = Logger.getLogger("DSOShareObjectEventHandler");

    private DSOServer dsoServer;
    private DSOServerThread dsoServerThread;

    public DSOShareObjectEventHandler(DSOServer dsoServer, DSOServerThread dsoServerThread) {
        this.dsoServer = dsoServer;
        this.dsoServerThread = dsoServerThread;
    }

    @Override
    public void handleEvent(DSOEvent event) {
        log.info("*** handle share");
        DSO.updateLocal(((DSOShareObjectEvent) event).getObject());
        dsoServer.propagate(dsoServerThread, event);
    }
}
