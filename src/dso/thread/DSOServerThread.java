package dso.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

import dso.event.DSOEvent;
import dso.event.DSOJoinEvent;
import dso.event.DSOLockEvent;
import dso.event.DSONoopEvent;
import dso.event.DSOShareObjectEvent;
import dso.event.DSOUnlockEvent;
import dso.event.handler.server.DSOEventHandler;
import dso.event.handler.server.DSOJoinEventHandler;
import dso.event.handler.server.DSOLockEventHandler;
import dso.event.handler.server.DSONoopEventHandler;
import dso.event.handler.server.DSOShareObjectEventHandler;
import dso.event.handler.server.DSOUnlockEventHandler;

public class DSOServerThread extends SocketWriter {

    private final DSOServer dsoServer;
    private static final Logger log = Logger.getLogger("DSOServerThread");

    private final HashMap<Class<? extends DSOEvent>, DSOEventHandler> serverEventHandlers = new HashMap<Class<? extends DSOEvent>, DSOEventHandler>();

    public DSOServerThread(DSOServer dsoServer, Socket socket) throws IOException {
        super(socket);
        this.dsoServer = dsoServer;
        this.dsoServer.appendServerThread(this);
        serverEventHandlers.put(DSOLockEvent.class, new DSOLockEventHandler(dsoServer, this));
        serverEventHandlers.put(DSOUnlockEvent.class, new DSOUnlockEventHandler(dsoServer, this));
        serverEventHandlers.put(DSOJoinEvent.class, new DSOJoinEventHandler(dsoServer, this));
        serverEventHandlers.put(DSONoopEvent.class, new DSONoopEventHandler());
        serverEventHandlers.put(DSOShareObjectEvent.class, new DSOShareObjectEventHandler(dsoServer, this));
    }

    @Override
    public void handleEvent(DSOEvent event) {
        if (null == event) {
            log.warning("Event is null");
            return;
        }
        try {
            DSOEventHandler handler = serverEventHandlers.get(event.getClass());
            if (null == handler) {
                throw new IllegalStateException("Unable to resolve envent handler for " + event.getClass());
            }
            handler.handleEvent(event);
        } catch (Exception e) {
            handleError(e);
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
