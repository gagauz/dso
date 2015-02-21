package com.gagauz.dso.thread;

import com.gagauz.dso.event.DSOEvent;
import com.gagauz.dso.event.api.DSOEventProcessor;
import com.gagauz.dso.event.error.DSOEventException;
import com.gagauz.dso.stream.api.SocketObjectReader;
import com.gagauz.dso.stream.impl.io.DSOSocketReader;

import java.net.Socket;

public class SocketReaderThread extends Thread {

    private final SocketObjectReader reader;
    private final DSOEventProcessor eventProcessor;

    public SocketReaderThread(Socket socket, DSOEventProcessor eventProcessor) {
        this.reader = new DSOSocketReader(socket);
        this.eventProcessor = eventProcessor;
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
                    eventProcessor.handleEvent((DSOEvent) event);
                } else {
                    throw new DSOEventException("The object is not DSOEvent instance " + event.getClass());
                }
            } catch (Exception e) {
                eventProcessor.handleError(e);
            }
        }
    }
}
