package dso.event.handler;

import dso.event.DSOEvent;
import dso.thread.DSOServer;
import dso.thread.DSOServerThread;

public class DSOJoinEventHandler implements DSOEventHandler {

    private DSOServer dsoServer;
    private DSOServerThread dsoServerThread;

    public DSOJoinEventHandler(DSOServer dsoServer, DSOServerThread dsoServerThread) {
    }

    @Override
    public void handleEvent(DSOEvent event) {
        System.out.println("HANDLE JOIN EVENT / " + dsoServerThread);
    }

}
