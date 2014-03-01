package dso.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import dso.DSObject;
import dso.event.DSOEvent;
import dso.event.DSOSyncRootEvent;

public class DSOServer {

    public static boolean SERVER = false;

    private static final Logger log = Logger.getLogger("DSOServer");

    private static final Map<Class<?>, DSObject> objectMap = new HashMap<Class<?>, DSObject>();
    private static final Map<String, Boolean> lockMap = new ConcurrentHashMap<String, Boolean>();
    private static final Map<String, DSOServerThread> slaveNodes = new ConcurrentHashMap<String, DSOServerThread>();

    protected void putRoot(DSOServerThread initiator, DSObject value) {
        synchronized (objectMap) {
            log.info("Put root from initiator " + initiator.getName());
            objectMap.put(value.getClass(), value);
            for (DSOServerThread node : slaveNodes.values()) {
                if (!node.equals(initiator)) {
                    log.info("Propagate root to slave " + node.getName());
                    node.send(new DSOSyncRootEvent(value));
                }
            }
        }
    }

    protected Object getRoot(Class<?> key) {
        return objectMap.get(key);
    }

    protected boolean lock(String key) {
        if (lockMap.containsKey(key)) {
            return false;
        }
        log.info("== put lock " + key);
        lockMap.put(key, true);
        return true;
    }

    protected void unlock(String key) {
        log.info("== remove lock " + key);
        lockMap.remove(key);
    }

    private ServerSocket server;
    private boolean run = true;

    public void stopServer() {
        run = false;
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startServer() {
        SERVER = true;
        try {
			int port = 9999;
			server = new ServerSocket(port);
            String host = server.getInetAddress().getHostName();
			log.info("**** Start server on " + host + ":" + port + " ****");
            while (run) {
                Socket socket = server.accept();
                new DSOServerThread(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	protected void joinNode(DSOServerThread thread) {
        System.out.println("Joined " + thread.getName());
        slaveNodes.put(thread.getName(), thread);
	}

	protected void disconnectNode(DSOServerThread thread) {
        slaveNodes.remove(thread.getName());
	}

    public void push(DSOEvent dsoEvent) {
        for (DSOServerThread thread : slaveNodes.values()) {
            thread.send(dsoEvent);
        }
    }

}
