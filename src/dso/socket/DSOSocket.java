package dso.socket;

import static dso.utils.StreamUtils.writeBytes;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dso.Command;
import dso.utils.Call;
import dso.utils.StreamUtils;

public class DSOSocket {

    private Socket socket;

    private final Lock rLock = new ReentrantLock(true);

    private final Lock wLock = new ReentrantLock(true);

    public DSOSocket(Socket socket) {
        System.out.println("*************************************************************");
        System.out.println("********* DSO Soket ************");
        System.out.println("*************************************************************");
        this.socket = socket;
        try {
            this.socket.setKeepAlive(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public <V> V atomic(Call<DSOSocket, V> call) {
        try {
            return call.call(this);
        } finally {
        }
    }

    public synchronized void writeCommand(Command command) {
        writeString(command.name());
    }

    public synchronized void writeString(String string) {
        wLock.lock();
        try {
            writeBytes(socket.getOutputStream(), string.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wLock.unlock();
        }
    }

    public void writeObject(Object obj) {
        wLock.lock();
        try {
            StreamUtils.writeObject(socket.getOutputStream(), obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wLock.unlock();
        }
    }

    public Object readObject() {
        rLock.lock();
        try {
            return StreamUtils.readObject(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return null;
    }

    public synchronized Command readCommand() {
        String string = readString();
        return Command.detect(string);
    }

    public synchronized String readString() {
        rLock.lock();
        try {
            return StreamUtils.readString(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return null;
    }

    public void shutdownInput() {
        try {
            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
        }
        socket = null;
    }

    public boolean isOpen() {
        try {
            socket.getInputStream().mark(1);
            if (socket.getInputStream().read() < 0) {
                return false;
            }

            socket.getInputStream().reset();
        } catch (SocketException e) {
            return false;
        } catch (IOException e) {
        }
        return true;
    }
}
