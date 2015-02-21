package com.gagauz.dso.stream.impl.io;

import com.gagauz.dso.stream.api.SocketObjectWriter;
import com.gagauz.dso.utils.StreamUtils;


import java.io.OutputStream;
import java.net.Socket;

public class DSOSocketWriter implements SocketObjectWriter {

    private final OutputStream wStream;

    public DSOSocketWriter(Socket socket) {
        System.out.println("*************************************************************");
        System.out.println("****** Create DSOSocketWriter " + socket.getInetAddress() + ":" + socket.getPort() + " ************");
        System.out.println("*************************************************************");
        try {
            wStream = socket.getOutputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void writeObject(Object obj) {
        try {
            StreamUtils.writeObject(wStream, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
