package dso.thread;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import dso.event.DSOEvent;
import dso.event.api.DSOEventProcessor;
import dso.stream.api.SocketObjectWriter;
import dso.stream.impl.io.DSOSocketWriter;

abstract public class SocketWriter implements DSOEventProcessor {

    private static final Logger log = Logger.getLogger("DSOClientServer");

    protected final Socket socket;
    protected SocketObjectWriter writer;
    protected SocketReaderThread readerThread;

    public SocketWriter(Socket socket) {
        this.socket = socket;
        this.writer = new DSOSocketWriter(socket);
    }

    public void send(DSOEvent<?> event) {
        try {
            writer.writeObject(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    abstract protected void handleEventInternal(DSOEvent<?> event);

    @Override
    public void handleEvent(DSOEvent event) {
        if (null == event) {
            log.warning("Event is null");
            return;
        }
        try {
            handleEventInternal(event);
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
