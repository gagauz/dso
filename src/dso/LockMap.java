package dso;

import java.util.HashMap;
import java.util.HashSet;

public class LockMap {
    private final HashMap<Class<?>, HashMap<Integer, HashSet<String>>> hierarchyMap = new HashMap<Class<?>, HashMap<Integer, HashSet<String>>>();

    public synchronized boolean tryLock(Object object, String method) {
        if (null != object) {
            if (null == method) {
                method = "_dso_NULL";
            }

            HashMap<Integer, HashSet<String>> objects = hierarchyMap.get(object.getClass());
            if (null != objects) {
                HashSet<String> methods = objects.get(object.hashCode());
                if (null != methods) {
                    return methods.add(method);
                } else {
                    methods = new HashSet<String>();
                    objects.put(object.hashCode(), methods);
                    hierarchyMap.put(object.getClass(), objects);
                }
            } else {
                HashSet<String> methods = new HashSet<String>();
                methods.add(method);
                objects = new HashMap<Integer, HashSet<String>>();
                objects.put(object.hashCode(), methods);
                hierarchyMap.put(object.getClass(), objects);
            }
        }

        return true;
    }

    public synchronized void unlock(Object object, String method) {
        if (null != object) {
            if (null == method) {
                method = "_dso_NULL";
            }

            HashMap<Integer, HashSet<String>> objects = hierarchyMap.get(object.getClass());
            if (null != objects) {
                HashSet<String> methods = objects.get(object.hashCode());
                if (null != methods) {
                    methods.remove(method);
                }
            }
        }
    }

}
