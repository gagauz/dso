package dso.thread;

import dso.event.DSOEvent;
import dso.event.DSOJoinEvent;
import dso.event.DSOLockEvent;
import dso.event.DSOUnlockEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class DSOServerThread extends SocketWriter {

    private final DSOServer dsoServer;
    private static final Logger log = Logger.getLogger("DSOServerThread");

    public DSOServerThread(DSOServer dsoServer, Socket socket) throws IOException {
        super(socket);
        this.dsoServer = dsoServer;
        this.dsoServer.appendServerThread(this);
    }

    @Override
    public void closeSocket() {
        dsoServer.removeServerThread(this);
        super.closeSocket();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null != obj && obj instanceof DSOServerThread) {
            return ((DSOServerThread) obj).readerThread.equals(readerThread);
        }
        return false;
    }

    public DSOServer getDsoServer() {
        return dsoServer;
    }

    @Override
    protected void handleEventInternal(DSOEvent event) {
        if (event instanceof DSOJoinEvent) {
            System.out.println("HANDLE JOIN EVENT / " + this);
        } else if (event instanceof DSOLockEvent) {
            log.info("** Handle lock event");
            DSOLockEvent lockEvent = (DSOLockEvent) event;
            lockEvent.setNodeId(hashCode());
            getDsoServer().lock(lockEvent);
        } else if (event instanceof DSOUnlockEvent) {
            log.info("** Handle unlock event");
            DSOUnlockEvent lockEvent = (DSOUnlockEvent) event;
            getDsoServer().unlock(lockEvent);
        }
    }
}
