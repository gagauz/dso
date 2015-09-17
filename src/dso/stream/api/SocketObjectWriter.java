package dso.stream.api;

import java.io.IOException;

public interface SocketObjectWriter {
    void writeObject(Object obj) throws IOException;
}
