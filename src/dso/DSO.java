package dso;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import dso.thread.DSOClient;
import dso.thread.DSOServer;

public class DSO {

    private static final Logger log = Logger.getLogger("DSOClient");

    private static final Map<String, Object> objectMap = new HashMap<String, Object>();

    private static DSOClient client;

    static {
        if (!DSOServer.SERVER) {
            client = new DSOClient("0.0.0.0", 9999);
        }
    }

    public static void update(DSObject object) {
        log.info("Put object to map");
        objectMap.put(object.__hash(), object);
    }

    public static void propagate(DSObject object) {
        System.out.println("Push object to server");
        client.pushRoot(object);
    }
}
