package dso.cluster;

public class Client extends Node {

    private static final long serialVersionUID = 1L;

    public Client(int id, String address) {
        super("Client-" + id, address);
    }

    @Override
    public boolean isServer() {
        return false;
    }

}
