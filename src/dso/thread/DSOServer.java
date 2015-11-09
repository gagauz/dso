package dso.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dso.cluster.Cluster;
import dso.cluster.Node;
import dso.event.DSOEvent;
import dso.event.JoinEvent;
import dso.event.LeaveEvent;
import dso.socket.api.ClientConnection;
import dso.socket.api.ConnectionFactory;
import dso.socket.api.ServerConnection;
import dso.socket.impl.io.IOConnectionFactory;

public class DSOServer implements Runnable, DSOProcessor {

    public static boolean SERVER = false;

    private static final Logger log = Logger.getLogger("DSOServer");

    private static final List<ServerThread> slaveNodes = new ArrayList<>();
    private static final ConnectionFactory connetcionFactory = new IOConnectionFactory();
    private ServerConnection serverConnection;
    private Thread serverThread;
    private Node serverNode;

    public void stopServer() {
        if (null != serverConnection) {
            Cluster cluster = Cluster.removeNode(serverNode);
            propagate(new LeaveEvent(cluster));
            for (ServerThread node : new ArrayList<ServerThread>(slaveNodes)) {
                node.closeSocket();
            }
            try {
                serverConnection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverThread.interrupt();
        }
    }

    public void startServer() {
        if (null != serverThread) {
            throw new IllegalStateException("ServerThread is already assigned. Don't start server twice!");
        }
        int port = 9999;
        try {
            serverConnection = connetcionFactory.createServer(port);
            String address = serverConnection.getAddress();
            Cluster cluster = Cluster.addServer(address);
            serverNode = cluster.getNode();
            log.info("**** Start server on " + address + " ****");
        } catch (Exception e) {
            throw new RuntimeException("Failed to start server", e);
        }
        serverThread = new Thread(this);
        serverThread.start();
    }

    @Override
    public void run() {
        try {
            // Wait for the client connections
            while (!serverThread.isInterrupted()) {
                log.info("**** Waiting for clients... ****");
                ClientConnection socket = serverConnection.acceptClientConnection();
                log.info("**** Accepted client " + socket.getSocket().getRemoteSocketAddress() + "... ****");
                new ServerThread(socket.getSocket());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopServer();
        }
    }

    public void propagate(ServerThread initiator, DSOEvent dsoEvent) {
        log.info("Propagate to slaves " + dsoEvent.getClass());
        for (ServerThread slave : slaveNodes) {
            if (!slave.equals(initiator)) {
                log.info("Client <<< Push event to slave " + slave.hashCode());
                slave.send(dsoEvent);
            }
        }
    }

    public void propagate(DSOEvent dsoEvent) {
        log.info("Propagate to slaves " + dsoEvent.getClass());
        for (ServerThread slave : slaveNodes) {
            log.info("Client <<< Push event to slave " + slave.hashCode());
            slave.send(dsoEvent);
        }
    }

    public void pushToNode(int nodeId, DSOEvent dsoEvent) {
        log.info("Propagate to one slave " + dsoEvent.getClass());
        ServerThread slave = slaveNodes.get(nodeId);
        if (null != slave) {
            log.info("Client <<< Push event to slave " + slave.hashCode());
            slave.send(dsoEvent);
        } else {
            throw new IllegalStateException("Unknown node ID " + nodeId);
        }
    }

    class ServerThread extends SocketWriter {
        private final Node node;

        private ServerThread(Socket socket) throws IOException {
            super(socket);
            // Make snapshot of current cluster state
            Cluster cluster = Cluster.addClient(socket.getInetAddress().getHostName());
            node = cluster.getNode();
            System.out.println("*** Node " + node.getNodeId() + " connected to the server. Starting thread for this node.");
            slaveNodes.add(this);
            startReader();
            propagate(new JoinEvent(cluster));
        }

        @Override
        public void closeSocket() {
            System.out.println("*** Node " + node.getNodeId() + " left cluster");
            slaveNodes.remove(this);
            super.closeSocket();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (null != obj && obj instanceof ServerThread) {
                return ((ServerThread) obj).readerThread.equals(readerThread);
            }
            return false;
        }

        @Override
        protected void handleEventInternal(DSOEvent event) {
            log.info("Handle event " + event);
        }
    }

}
