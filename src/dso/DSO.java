package dso;

import dso.cluster.DSONodeType;
import dso.object.DSOChange;
import dso.object.DSObject;
import dso.thread.DSOClient;
import dso.thread.DSOProcessor;
import dso.thread.DSOServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DSO {

    private static final Logger log = Logger.getLogger("DSO");

    private static final Map<Integer, Object> objectGlobalMap = new HashMap<Integer, Object>();
    private static final DSOMap globalMap = new DSOMap();
    private static final Map<Long, Map<String, List<DSOChange>>> lockMap = new HashMap<>();

    private static volatile long UID_COUNTER = 0;
    private static Object uidCounterLock = new Object();

    public static DSOProcessor sharer;

    public static final Map<String, Object> rootMap = new HashMap<>();

    static {
        if (DSONodeType.ALWAYS.isCurrent()) {
            log.info("*** This node is always master!");
            startAsServer();
        } else if (DSONodeType.NEVER.isCurrent()) {
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
        objectGlobalMap.put(object.hashCode(), object);
    }

    public static void share(Object object) {
        log.info("Share object");
        sharer.share(object);
    }

    public static void lock(DSObject object, String methodName) {
        Map map = lockMap.get(object.__get_dso_UID());
        if (null != map) {
            if (null != map.get(methodName)) {
                return;
            }
        }
        log.info("Lock on object " + object.getClass().getName() + '.' + methodName);
        sharer.lock(object, methodName);
    }

    public static void unlock(Object object, String string) {
        log.info("Unlock on object " + object + " " + string);
        sharer.unlock(object, string);
    }

    public static void checkFieldAccessBegin(Object object, String string) {
        log.info("Check access begin " + object + " " + string);
    }

    public static void checkFieldAccessEnd(Object object, String string) {
        log.info("Check access end " + object + " " + string);
    }

    public static void noop() {
        log.info("Noop");
        sharer.noop();
    }

    public static void putClusteredObject(String key, Object value) {
        log.info("putRoot [" + key + "] " + value);
        rootMap.put(key, value);
    }

    public static Object getClusteredObject(String key) {
        log.info("getRoot [" + key + "]");
        return rootMap.get(key);
    }

    public static void recordChange(Object object, String fieldName, Object newValue) {

    }

    public static long allocateId() {
        synchronized (uidCounterLock) {
            return ++UID_COUNTER;
        }
    }

    public static void deallocateId() {
        synchronized (uidCounterLock) {
            --UID_COUNTER;
        }
    }
}
