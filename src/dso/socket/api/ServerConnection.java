package dso.socket.api;

import java.io.IOException;

public interface ServerConnection {
    ClientConnection acceptClientConnection() throws IOException;

    String getAddress() throws IOException;

    void close() throws IOException;
}
