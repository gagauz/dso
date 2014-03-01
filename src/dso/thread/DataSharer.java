package dso.thread;

public interface DataSharer {

    void share(Object object);

    void lock(Object object, String name);

    void unlock(Object object, String name);
}
