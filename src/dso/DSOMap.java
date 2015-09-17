package dso;

import dso.object.DSObject;

import java.util.HashMap;
import java.util.Map;

public class DSOMap {
    private static final Map<Long, DSObject> MAP = new HashMap<>();

    public void put(DSObject object) {
        MAP.put(object.__get_dso_UID(), object);
    }

    public Object get(long uid) {
        return MAP.get(uid);
    }
}
