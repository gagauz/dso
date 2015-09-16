package dso;

import dso.cluster.DSONodeMasterType;
import dso.thread.DSOClient;
import dso.thread.DSOProcessor;
import dso.thread.DSOServer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DSO {

    private static final Logger log = Logger.getLogger("DSO");

    private static final Map<Integer, Object> objectMap = new HashMap<Integer, Object>();

    public static DSOProcessor sharer;

    static {
        if (DSONodeMasterType.ALWAYS.isCurrent()) {
            log.info("*** This node is always master!");
            startAsServer();
        } else if (DSONodeMasterType.NEVER.isCurrent()) {
            log.info("*** This node is always slave!");
            startAsClient();
        } else {
            try {
                startAsServer();
            } catch (Exception e) {
                e.printStackTrace();
                startAsClient();
            }
        }
        log.info("* Started");
    }

    private static void startAsServer() {
        log.info("* Trying to start as server...");
        try {
            DSOServer server = new DSOServer();
            server.startServer();
            sharer = server;
        } catch (Exception e) {
            throw new RuntimeException("Unable to start as server", e);
        }
    }

    private static void startAsClient() {
        log.info("* Trying to start as client...");
        try {
            DSOClient client = new DSOClient();
            sharer = client;
        } catch (Exception e) {
            throw new RuntimeException("Unable to start as server", e);
        }
    }

    public static void updateLocal(Object object) {
        log.info("Update object local");
        objectMap.put(object.hashCode(), object);
    }

    public static void share(Object object) {
        log.info("Share object");
        sharer.share(object);
    }

    public static void lock(Object object, String string) {
        log.info("Lock on object " + string);
        sharer.lock(object, string);
    }

    public static void unlock(Object object, String string) {
        log.info("Unlock on object " + object + " " + string);
        sharer.unlock(object, string);
    }

	public static void noop() {
		log.info("Noop");
		sharer.noop();
	}
}
