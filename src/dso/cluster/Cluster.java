package dso.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cluster implements Serializable {

    private static final long serialVersionUID = -6303043907580257548L;
    private static Node _node;
    private static List<Node> _nodes = new ArrayList<>();
    private static int _nodesCount = 0;

    private List<Node> nodes = new ArrayList<>();
    private Node node;

    public static List<Node> getAllNodes() {
        return Collections.unmodifiableList(_nodes);
    }

    public static Node getThisNode() {
        return _node;
    }

    // Make snapshot of current cluster state
    public static Cluster addClient(String address) {
        synchronized (_nodes) {
            _nodesCount++;
            Client node = new Client(_nodesCount, address);
            addNode(node);
            return instance(node);
        }
    }

    public static Cluster addServer(String address) {
        synchronized (_nodes) {
            Server node = new Server(address);
            addNode(node);
            return instance(node);
        }
    }

    private static void addNode(Node node) {
        _nodes.add(node);
    }

    public static Cluster removeNode(Node node) {
        synchronized (_nodes) {
            if (node.isClient())
                _nodesCount--;
            _nodes.remove(node);
            return instance(node);
        }
    }

    private static Cluster instance(Node forNode) {
        Cluster cluster = new Cluster();
        cluster.nodes = _nodes;
        cluster.node = forNode;
        return cluster;
    }

    public Node getNode() {
        return node;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public static void accept(Cluster msg) {
        _node = msg.getNode();
        _nodes = msg.getNodes();
    }

}
