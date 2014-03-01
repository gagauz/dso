package dso.event;

import java.io.Serializable;

import dso.DSOEventCallback;

public class DSOEvent implements Serializable {
    private static int counter = 0;
    private static final long serialVersionUID = -6019854120787832156L;
    private int uid = 0;

    public DSOEvent() {
        counter++;
    }

    @Override
    public int hashCode() {
        if (0 == uid) {
            uid = counter;
        }
        return uid;
    }

    @Override
    public String toString() {
        return getClass().getName() + "#" + hashCode();
    }

    public DSOEventCallback callback() {
        return null;
    }

    public boolean locked() {
        return false;
    }
}
