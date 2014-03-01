package dso.thread;

import dso.LockMap;
import dso.event.DSOEvent;
import dso.event.DSOLockEvent;
import dso.event.DSOShareObjectEvent;
import dso.socket.api.ClientConnection;
import dso.socket.api.ConnectionFactory;
import dso.socket.api.ServerConnection;
import dso.socket.impl.io.IOConnectionFactory;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DSOServer extends Thread implements DataSharer {

    public static boolean SERVER = false;

    private static final Logger log = Logger.getLogger("DSOServer");

    private static final LockMap lockMap = new LockMap();
    private static ArrayDeque<DSOLockEvent> lockWaitingQueue = new ArrayDeque<DSOLockEvent>();
    private static final Map<Integer, DSOServerThread> slaveNodes = new ConcurrentHashMap<Integer, DSOServerThread>();
    private static final ConnectionFactory connetcionFactory = new IOConnectionFactory();
    private ServerConnection server;

    public DSOServer() {
        setDaemon(true);
    }

    @Override
    public synchronized void start() {
        throw new IllegalStateException("Don't call start on DSOServer thread, use startServer() method!");
    }

    public void stopServer() {
        if (server != null) {
            for (DSOServerThread node : new ArrayList<DSOServerThread>(slaveNodes.values())) {
                node.closeSocket();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            interrupt();
        }
    }

    public void startServer() {
        int port = 9999;
        try {
            server = connetcionFactory.createServer(port);
            String addr = server.getAddress();
            log.info("**** Start server on " + addr + " ****");
        } catch (Exception e) {
            throw new RuntimeException("Failed to start server", e);
        }
        super.start();
    }

    @Override
    public void run() {
        try {

            while (!isInterrupted()) {
                log.info("**** Waiting for clients... ****");
                ClientConnection socket = server.accept();
                new DSOServerThread(this, socket.getSocket());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopServer();
        }
    }

    public void propagate(DSOServerThread initiator, DSOEvent dsoEvent) {
        log.info("Propagate to slaves ...");
        for (DSOServerThread slave : slaveNodes.values()) {
            if (!slave.equals(initiator)) {
                log.info("Client <<< Push event to slave " + slave.hashCode());
                slave.send(dsoEvent);
            }
        }
    }

    public void appendServerThread(DSOServerThread dsoServerThread) {
        System.out.println("*** Node " + dsoServerThread.hashCode() + " joined cluster");
        slaveNodes.put(dsoServerThread.hashCode(), dsoServerThread);
        dsoServerThread.startReader();
    }

    public void removeServerThread(DSOServerThread dsoServerThread) {
        System.out.println("*** Node " + dsoServerThread.hashCode() + " left cluster");
        slaveNodes.remove(dsoServerThread.hashCode());
    }

    @Override
    public void share(Object object) {
        propagate(null, new DSOShareObjectEvent(object));
    }

    @Override
    public void lock(Object object, String method) {
        log.info("server lock " + object + " : " + method);
        while (!lockMap.tryLock(object, method)) {
            Thread.yield();
        }
    }

    @Override
    public void unlock(Object object, String method) {
        log.info("server unlock " + object + " : " + method);
        lockMap.unlock(object, method);
    }

    public void addLockInQueue(DSOLockEvent lockEvent) {
        log.info("Add lock event in queue");
        lockWaitingQueue.addLast(lockEvent);
    }

    private void checkLockInQueue() {
        for (DSOLockEvent lockEvent : new ArrayDeque<DSOLockEvent>(lockWaitingQueue)) {
            if (lockMap.tryLock(lockEvent.getClassName(), lockEvent.getObjectHash(), lockEvent.getMethodName())) {

            }
        }
    }
}
