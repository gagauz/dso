package dso.thread;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import dso.DSObject;
import dso.event.DSOJoinEvent;
import dso.event.DSOLockedEvent;
import dso.event.DSOSyncRootEvent;
import dso.socket.DSOSocket;

public class DSOClient extends DSOClientServer {

    private static final Logger log = Logger.getLogger("DSOClient");

    public DSOClient() {
        this("0.0.0.0", 9999);
    }

    public DSOClient(String host, int port) {
        try {
            join(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public DSOClient(InetAddress addr, int port) {
        join(addr, port);
    }

    public void join(InetAddress addr, int port) {
        log.info("Connecting to " + addr + ":" + port + "...");
        while (true) {
            try {
                socket = new DSOSocket(new Socket(addr, port));
                send(new DSOJoinEvent(this));
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.severe("Can't connetct to " + addr + ":" + port
                    + ". Trying to reconnect...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
        }
        startListening();
    }

    public void leave() {
        stopListening();
        socket.close();
        log.info("Disconnected " + name);
    }

    public void pushRoot(DSObject object) {
        send(new DSOSyncRootEvent(object));
    }

    public void lockedOperation(DSOLockedEvent event) {
        send(event);
    }
}
