package dso.event;

import dso.cluster.Cluster;

public class JoinEvent extends DSOEvent<Cluster> {

    private static final long serialVersionUID = 6097388935907840367L;

    public JoinEvent(Cluster msg) {
        super(msg);
    }

}
