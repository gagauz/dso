package dso.stream.impl.io;

import dso.stream.api.SocketObjectReader;
import dso.utils.StreamUtils;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DSOSocketReader implements SocketObjectReader {

    private final InputStream rStream;

    public DSOSocketReader(Socket socket) {
        System.out.println("*************************************************************");
        System.out.println("****** Create DSOSocketReader " + socket.getInetAddress() + ":" + socket.getPort() + " ******");
        System.out.println("*************************************************************");
        try {
            this.rStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized Object readObject() throws Exception {
        return StreamUtils.readObject(rStream);
    }
}
