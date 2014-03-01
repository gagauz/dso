package dso.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import dso.DSO;
import dso.DSOEventCallback;
import dso.DSOEventHandler;
import dso.event.DSOEvent;
import dso.event.DSOSyncRootEvent;
import dso.socket.DSOSocket;

public class DSOClientServer implements DSOEventHandler {

    private static final Logger log = Logger.getLogger("DSOClientServer");

    protected String name = "";
    protected DSOSocket socket;
    protected DSOReadThread readThread;
    
    protected Map<Integer, DSOEventCallback> callbacks = new ConcurrentHashMap<Integer, DSOEventCallback>();
    
    public void send(DSOEvent event) {
        if (event.callback() != null) {
            log.info(Thread.currentThread().getName() + " store callback " + event);
            callbacks.put(event.hashCode(), event.callback());
        }
        socket.writeObject(event);
    }

    public void back(DSOEvent event) {
        socket.writeObject(event);
    }

    @Override
    public void handle(DSOEvent event) {
        log.info(Thread.currentThread().getName() + " handle " + event);
        if (event instanceof DSOSyncRootEvent) {
            DSO.update(((DSOSyncRootEvent) event).getObject());
            return;
        }
        DSOEventCallback callback = callbacks.remove(event.hashCode());
        if (null != callback) {
            log.info(Thread.currentThread().getName() + " got callback for " + event);
            callback.callOn(event);
        }
    }

    public void startListening() {
        readThread = new DSOReadThread(socket, this);
        readThread.start();
        name = readThread.getName();
    }

    public void stopListening() {
        readThread.cancel();
        socket.shutdownInput();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void handleDisconnect() {
    }
}
