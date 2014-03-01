package dso.thread;

import dso.event.error.DSOEventErrorHandler;

import dso.event.DSOEvent;
import dso.event.handler.server.DSOEventHandler;
import dso.stream.api.SocketObjectReader;
import dso.stream.impl.io.DSOSocketReader;

import java.net.Socket;

public class SocketReaderThread extends Thread {

    private final SocketObjectReader reader;
    private final DSOEventHandler eventHandler;
    private final DSOEventErrorHandler errorHandler;

    public SocketReaderThread(Socket socket, DSOEventHandler eventHandler, DSOEventErrorHandler errorHandler) {
        this.reader = new DSOSocketReader(socket);
        this.eventHandler = eventHandler;
        this.errorHandler = errorHandler;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Object event = reader.readObject();
                if (null == event) {
                    continue;
                }
                if (event instanceof DSOEvent) {
                    //Handle
                    eventHandler.handleEvent((DSOEvent) event);
                } else {
                    throw new IllegalStateException("The object is not DSOEvent instance " + event.getClass());
                }
            } catch (Exception e) {
                errorHandler.handleError(e);
            }
        }
    }
}
