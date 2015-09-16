package dso.thread;

import dso.event.DSOEvent;
import dso.event.api.DSOEventHandler;
import dso.event.api.DSOEventProcessor;
import dso.event.handler.DSOEventHandlerResolver;
import dso.stream.api.SocketObjectWriter;
import dso.stream.impl.io.DSOSocketWriter;

import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

abstract public class SocketWriter implements DSOEventProcessor {

    private static final Logger log = Logger.getLogger("DSOClientServer");

    protected final Socket socket;
    protected SocketObjectWriter writer;
    protected SocketReaderThread readerThread;

    public SocketWriter(Socket socket) {
        this.socket = socket;
        this.writer = new DSOSocketWriter(socket);
    }

    public void send(DSOEvent event) {
        writer.writeObject(event);
    }

    abstract protected DSOEventHandlerResolver getEventHandlerResolver();

    @Override
    public void handleEvent(DSOEvent event) {
        if (null == event) {
            log.warning("Event is null");
            return;
        }
        try {
            DSOEventHandler handler = getEventHandlerResolver().resolve(event);
            handler.handleEvent(event);
        } catch (Exception e) {
            handleError(e);
        }
    }

    @Override
    public void handleError(Exception error) {
        if (error instanceof SocketException) {
            closeSocket();
        } else {
            error.printStackTrace();
        }
    }

    public void startReader() {
        readerThread = new SocketReaderThread(socket, this);
        readerThread.start();
    }

    public void stopReader() {
        readerThread.interrupt();
    }

    public void closeSocket() {
        System.out.println("*********************************************************");
        System.out.println("********** Close socket in SocketWriter " + hashCode() + " ********");
        System.out.println("*********************************************************");
        try {
            stopReader();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
