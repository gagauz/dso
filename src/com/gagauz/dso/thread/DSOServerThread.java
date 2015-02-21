package com.gagauz.dso.thread;

import com.gagauz.dso.event.handler.DSOEventHandlerResolver;
import com.gagauz.dso.event.handler.server.DSOServerEventHandlerResolver;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class DSOServerThread extends SocketWriter {

    private final DSOServer dsoServer;
    private static final Logger log = Logger.getLogger("DSOServerThread");

    private final DSOEventHandlerResolver serverEventHandlerResolver;

    public DSOServerThread(DSOServer dsoServer, Socket socket) throws IOException {
        super(socket);
        this.dsoServer = dsoServer;
        this.dsoServer.appendServerThread(this);
        this.serverEventHandlerResolver = new DSOServerEventHandlerResolver(this);
    }

    @Override
    protected DSOEventHandlerResolver getEventHandlerResolver() {
        return serverEventHandlerResolver;
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
}
