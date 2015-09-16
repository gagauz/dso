package dso.event.handler.server;

import dso.DSO;
import dso.event.DSOEvent;
import dso.event.DSOShareObjectEvent;
import dso.event.api.DSOEventHandler;
import dso.thread.DSOServerThread;

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
