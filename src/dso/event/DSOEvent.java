package dso.event;

import java.io.Serializable;

public class DSOEvent<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = -6019854120787832156L;
    private static int counter = 0;
    private final int uid;
    private final T msg;

    DSOEvent(T msg) {
        uid = ++counter;
        this.msg = msg;
    }

    public T getMsg() {
        return msg;
    }

    @Override
    public int hashCode() {
        return uid;
    }

    @Override
    public String toString() {
        return getClass().getName() + "#" + hashCode() + " {msg=" + getMsg() + "}";
    }

}
