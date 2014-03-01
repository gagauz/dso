package dso;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static dso.StreamUtils.*;

public class Client implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2395628921415789815L;

    private Logger log = Logger.getLogger("log");

    private Socket socket;
    private ServerSocket server;
    private boolean run = true;

    public void stopServer() {
        run = false;
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startServer() {
        try {

            server = new ServerSocket(9999);
            String host = server.getInetAddress().getHostName();
            log.info("**** Start server on " + host + " ****");
            while (run) {
                log.info("**** Waiting for connections ****");
                Socket socket = server.accept();
                log.info("**** Connection accepted ****");
                InputStream in = socket.getInputStream();
                //ObjectInputStream ois = new ObjectInputStream(in);
                OutputStream out = socket.getOutputStream();

                while (true) {
                    String str = readString(in);
                    long s = System.currentTimeMillis();
                    log.info("-- Read string : " + str);
                    if ("object>>>".equals(str)) {
                        Object obj = readObject(in);
                        s = s - System.currentTimeMillis();
                        if (null != obj) {
                            log.info("-- Read object : " + obj.getClass() + " " + s + " ms --");
                            log.info(obj.toString());
                            writeString(out, "<<<ok");
                        }
                    } else if (null == str) {
                        //Disconnected client
                        log.info("*** Close socket ***");
                        close(socket);
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void connect(String node) {
        try {
            log.info("**** Connecting to " + node + " ****");
            InetAddress addr = InetAddress.getLocalHost();
            socket = new Socket(addr, 9999);
            socket.setKeepAlive(true);
            log.info("**** Connected ****");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        log.info("**** Disconnecting ****");

        close(socket);
        log.info("**** Disconnected ****");
    }

    protected void send(String string) {
        try {
            writeBytes(socket.getOutputStream(), string.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String sendObject(Object obj) {
        try {
            writeBytes(socket.getOutputStream(), "object>>>".getBytes());
            long s = System.currentTimeMillis();
            writeObject(socket.getOutputStream(), obj);
            s = s - System.currentTimeMillis();
            log.info("* Object sent in " + s + " ms");
            return readString(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }
}
