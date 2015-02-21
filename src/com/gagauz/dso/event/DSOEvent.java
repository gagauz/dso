package com.gagauz.dso.event;

import java.io.Serializable;

public class DSOEvent implements Serializable {
    private static int counter = 0;
    private static final long serialVersionUID = -6019854120787832156L;
    private final int uid;

    public DSOEvent() {
        uid = ++counter;
    }

    @Override
    public int hashCode() {
        return uid;
    }

    @Override
    public String toString() {
        return getClass().getName() + "#" + hashCode();
    }
}
