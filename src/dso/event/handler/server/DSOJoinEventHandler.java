package dso.event.handler.server;

import dso.event.DSOEvent;
import dso.event.api.DSOEventHandler;
import dso.thread.DSOServerThread;

public class DSOJoinEventHandler implements DSOEventHandler {

    private DSOServerThread dsoServerThread;

    public DSOJoinEventHandler(DSOServerThread dsoServerThread) {
        this.dsoServerThread = dsoServerThread;
    }

    @Override
    public void handleEvent(DSOEvent event) {
        System.out.println("HANDLE JOIN EVENT / " + dsoServerThread);
    }

}
