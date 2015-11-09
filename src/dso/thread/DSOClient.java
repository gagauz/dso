package dso.thread;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import dso.cluster.Cluster;
import dso.event.DSOEvent;
import dso.event.JoinEvent;
import dso.socket.api.ConnectionFactory;
import dso.socket.impl.io.IOConnectionFactory;

public class DSOClient extends SocketWriter implements DSOProcessor {

    private static final Logger log = Logger.getLogger("DSOClient");

    private static final ConnectionFactory connectionFactory = new IOConnectionFactory();

    protected Set<Long> lockWaitingQueue = new HashSet<Long>();

    public DSOClient() {
        this("0.0.0.0", 9999);
    }

    public DSOClient(String addr, int port) {
        super(connect(addr, port));
        startReader();
    }

    private static Socket connect(String host, int port) {
        log.info("Connecting to " + host + ":" + port + "...");
        Socket socket = null;
        while (true) {
            try {
                socket = connectionFactory.createClient(host, port).getSocket();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.severe("Can't connetct to " + host + ":" + port + ". Trying to reconnect...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
        }
        return socket;
    }

    @Override
    protected void handleEventInternal(DSOEvent<?> event) {
        log.info("Handle event " + event);
        if (event instanceof JoinEvent) {
            Cluster.accept(((JoinEvent) event).getMsg());
            log.info("" + Cluster.getThisNode());
            log.info("" + Cluster.getAllNodes());
        }
    }
}
