package dso.thread;

public interface DSOProcessor {

    void lock(Object object, String name);

    void unlock(Object object, String name);
}
