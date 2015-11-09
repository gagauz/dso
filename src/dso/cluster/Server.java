package dso.cluster;

public class Server extends Node {

    private static final long serialVersionUID = 6986090523692143852L;

    public Server(String address) {
        super("Server", address);
    }

    @Override
    public boolean isServer() {
        return true;
    }
}
