package dso.thread;

import dso.DSOEventHandler;
import dso.event.DSOEvent;
import dso.socket.DSOSocket;

public class DSOReadThread extends Thread {
    private final DSOSocket socket;
    private final DSOEventHandler handler;
    private boolean run;

    public DSOReadThread(DSOSocket socket, DSOEventHandler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public synchronized void start() {
        run = true;
        super.start();
    }

    public void cancel() {
        run = false;
        socket.shutdownInput();
    }


    @Override
    public void run() {
        while (run) {
            Object event = socket.readObject();
            if (null == event) {
                if (!socket.isOpen()) {
                    handler.handleDisconnect();
                    break;
                }
                continue;
            }
            if (event instanceof DSOEvent) {
                //Handle
                handler.handle((DSOEvent) event);
            }
        }
    }
}
