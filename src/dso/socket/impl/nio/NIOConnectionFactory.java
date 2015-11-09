package dso.socket.impl.nio;

import dso.socket.api.ClientConnection;
import dso.socket.api.ConnectionFactory;
import dso.socket.api.ServerConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOConnectionFactory implements ConnectionFactory {

    @Override
    public ClientConnection createClient(final String host, final int port) throws IOException {
        return new ClientConnection() {
            final SocketChannel sChannel;
            {
                sChannel = SocketChannel.open();
                sChannel.configureBlocking(true);
                sChannel.connect(new InetSocketAddress(host, port));
            }

            @Override
            public Socket getSocket() {
                return sChannel.socket();
            }
        };
    }

    @Override
    public ServerConnection createServer(final int port) throws IOException {
        return new ServerConnection() {
            final ServerSocketChannel ssChannel;
            {
                ssChannel = ServerSocketChannel.open();
                ssChannel.configureBlocking(true);
                ssChannel.socket().bind(new InetSocketAddress(port));
            }

            @Override
            public ClientConnection acceptClientConnection() throws IOException {
                final SocketChannel socket = ssChannel.accept();
                return new ClientConnection() {
                    @Override
                    public Socket getSocket() {
                        return socket.socket();
                    }
                };
            }

            @Override
            public String getAddress() throws IOException {
                return "";// ssChannel.getLocalAddress().toString();
            }

            @Override
            public void close() throws IOException {
                ssChannel.close();
            }
        };
    }

}
