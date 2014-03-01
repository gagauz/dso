package dso.thread;

import dso.event.*;
import dso.event.handler.*;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

public class DSOServerThread extends SocketWriter {

    private final DSOServer dsoServer;
    private static final Logger log = Logger.getLogger("DSOServerThread");

    private final HashMap<Class<? extends DSOEvent>, DSOEventHandler> serverEventHandlers = new HashMap<Class<? extends DSOEvent>, DSOEventHandler>();

    public DSOServerThread(DSOServer dsoServer, Socket socket) throws IOException {
        super(socket);
        this.dsoServer = dsoServer;
        this.dsoServer.appendServerThread(this);
        serverEventHandlers.put(DSOLockEvent.class, new DSOLockEventHandler(dsoServer, this));

        serverEventHandlers.put(DSOJoinEvent.class, new DSOJoinEventHandler(dsoServer, this));

        serverEventHandlers.put(DSONoopEvent.class, new DSONoopEventHandler(dsoServer, this));

        serverEventHandlers.put(DSOShareObjectEvent.class, new DSOShareObjectEventHandler(dsoServer, this));
    }

    @Override
    public void handleEvent(DSOEvent event) {
        try {
            DSOEventHandler handler = serverEventHandlers.get(event.getClass());
            handler.handleEvent(event);
        } catch (Exception e) {
            handleError(e);
        }

        if (event instanceof DSOShareObjectEvent) {
            log.info("Client >>> Handle DSOShareObjectEvent");
            super.handleEvent(event);
            dsoServer.propagate(this, event);
            return;
        }
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
}
