package dso.socket.impl.io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import dso.socket.api.ClientConnection;
import dso.socket.api.ConnectionFactory;
import dso.socket.api.ServerConnection;

public class IOConnectionFactory implements ConnectionFactory {

    @Override
    public ClientConnection createClient(final String host, final int port) throws IOException {
        return new ClientConnection() {
            final Socket clientSocket;
            {
                clientSocket = new Socket(InetAddress.getByName(host), port);
            }

            @Override
            public Socket getSocket() {
                return clientSocket;
            }
        };
    }

    @Override
    public ServerConnection createServer(final int port) throws IOException {
        return new ServerConnection() {
            private final ServerSocket serverSocket;
            {
                serverSocket = new ServerSocket(port);
            }

            @Override
            public ClientConnection acceptClientConnection() throws IOException {
                final Socket socket = serverSocket.accept();
                return new ClientConnection() {
                    @Override
                    public Socket getSocket() {
                        return socket;
                    }
                };
            }

            @Override
            public String getAddress() {
                return serverSocket.getInetAddress().getHostName();
            }

            @Override
            public void close() throws IOException {
                serverSocket.close();
            }
        };
    }

}
