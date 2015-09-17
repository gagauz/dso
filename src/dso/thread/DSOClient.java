package dso.thread;

import dso.event.DSOEvent;
import dso.event.DSOJoinEvent;
import dso.event.DSOLockEvent;
import dso.event.DSOUnlockEvent;
import dso.socket.api.ConnectionFactory;
import dso.socket.impl.io.IOConnectionFactory;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

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
        send(new DSOJoinEvent(this));
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
    public void lock(Object object, String name) {
        // Request for lock, sleep until response
        log.info("> request for lock " + object + " " + name);
        requestForLock(Thread.currentThread(), object, name);
        while (lockWaitingQueue.contains(Thread.currentThread())) {
            log.info("..waiting..");
            Thread.yield();
        }
        log.info("<< lock granted");
    }

    @Override
    public void unlock(Object object, String name) {
        send(new DSOUnlockEvent(Thread.currentThread().getId(), object, name));
    }

    private void requestForLock(Thread thread, Object object, String name) {
        lockWaitingQueue.add(thread.getId());
        send(new DSOLockEvent(thread.getId(), object, name));
    }

    @Override
    protected void handleEventInternal(DSOEvent event) {
        if (event instanceof DSOLockEvent) {
            DSOLockEvent lockEvent = (DSOLockEvent) event;
            log.info(">> Lock request handle result" + lockEvent.getThreadId());
            lockWaitingQueue.remove(lockEvent.getThreadId());
        } else if (event instanceof DSOUnlockEvent) {
            DSOUnlockEvent lockEvent = (DSOUnlockEvent) event;
            log.info(">> Unlock request handle result " + lockEvent.getThreadId());
        }
    }
}
