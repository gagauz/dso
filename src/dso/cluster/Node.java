package dso.cluster;

import java.io.Serializable;

public abstract class Node implements Serializable {
    private static final long serialVersionUID = -6294156777566607764L;
    private final String id;
    private final String address;

    public Node(String id, String address) {
        this.id = id;
        this.address = address;
    }

    abstract boolean isServer();

    public final boolean isClient() {
        return !isServer();
    }

    public final String getNodeId() {
        return id;
    }

    public final String getAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(obj);
    }

    @Override
    public String toString() {
        return id;
    }
}
