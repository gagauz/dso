package dso;

import dso.event.DSOLockEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;


public class LockMap {

    private final ReentrantLock lock = new ReentrantLock(true);

    private final HashMap<String, HashMap<Integer, HashSet<String>>> hierarchyMap = new HashMap<String, HashMap<Integer, HashSet<String>>>();

    public boolean tryLock(DSOLockEvent event) {
        return tryLock(event.getClassName(), event.getObjectHash(), event.getMethodName());
    }

    public boolean tryLock(Object object, String objectMethod) {
        if (null != object) {
            return tryLock(object.getClass().getName(), object.hashCode(), objectMethod);
        }
        return true;
    }

    public void unlock(Object object, String objectMethod) {
        if (null != object) {
            unlock(object.getClass().getName(), object.hashCode(), objectMethod);
        }
    }

    public boolean tryLock(String objectClass, int objectHash, String objectMethod) {
        lock.lock();
        try {

            if (null == objectMethod) {
                objectMethod = "_dso_NULL";
            }

            HashMap<Integer, HashSet<String>> lockedObjects = hierarchyMap.get(objectClass);
            if (null != lockedObjects) {
                HashSet<String> lockedMethods = lockedObjects.get(objectHash);
                if (null != lockedMethods) {
                    return lockedMethods.add(objectMethod);
                } else {
                    lockedMethods = new HashSet<String>();
                    lockedObjects.put(objectHash, lockedMethods);
                    hierarchyMap.put(objectClass, lockedObjects);
                }
            } else {
                HashSet<String> methods = new HashSet<String>();
                methods.add(objectMethod);
                lockedObjects = new HashMap<Integer, HashSet<String>>();
                lockedObjects.put(objectHash, methods);
                hierarchyMap.put(objectClass, lockedObjects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return true;

    }

    public void unlock(String objectClass, int objectHash, String objectMethod) {
        lock.lock();
        try {

            if (null == objectMethod) {
                objectMethod = "_dso_NULL";
            }

            HashMap<Integer, HashSet<String>> objects = hierarchyMap.get(objectClass);
            if (null != objects) {
                HashSet<String> methods = objects.get(objectHash);
                if (null != methods) {
                    methods.remove(objectMethod);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
