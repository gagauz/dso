package dso.stream.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import dso.stream.api.SocketObjectWriter;
import dso.utils.StreamUtils;

public class DSOSocketWriter implements SocketObjectWriter {

    private final OutputStream wStream;

    public DSOSocketWriter(Socket socket) {
        System.out.println("+-----------------------------------------+");
        System.out.println("| Create DSOSocketWriter " + socket.getInetAddress() + ":" + socket.getPort() + " |");
        System.out.println("+-----------------------------------------+");
        try {
            wStream = socket.getOutputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void writeObject(Object obj) throws IOException {
        StreamUtils.writeObject(wStream, obj);
    }

}
