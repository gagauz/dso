package dso.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import dso.event.DSOEvent;
import dso.event.DSOJoinEvent;
import dso.event.DSOSyncRootEvent;
import dso.socket.DSOSocket;

public class DSOServerThread extends DSOClientServer {

    private final DSOServer dsoServer;

    private static final Logger log = Logger.getLogger("DSOServerThread");
    
    public DSOServerThread(DSOServer dsoServer, Socket socket) throws IOException {
        this.dsoServer = dsoServer;
        this.socket = new DSOSocket(socket);
        startListening();
        dsoServer.joinNode(this);
    }

    @Override
    public void handle(DSOEvent event) {
        if (event instanceof DSOJoinEvent) {
            log.info("Handle DSOJoinEvent");
            ((DSOJoinEvent) event).setName("client-for-" + getName());
            //Return
            log.info("Handle DSOJoinEvent");
            back(event);
            return;
        }

        if (event instanceof DSOSyncRootEvent) {
            log.info("Handle DSOSyncRootEvent");
            super.handle(event);
            dsoServer.putRoot(this, ((DSOSyncRootEvent) event).getObject());
            return;
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null != obj && obj instanceof DSOServerThread) {
            return ((DSOServerThread) obj).readThread.equals(readThread);
        }
        return false;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public void handleDisconnect() {
        super.handleDisconnect();
        dsoServer.disconnectNode(this);
        socket.close();
    }
}
