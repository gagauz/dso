package dso.event;

import dso.cluster.Cluster;

public class LeaveEvent extends DSOEvent<Cluster> {

    private static final long serialVersionUID = 6097388935907840367L;

    public LeaveEvent(Cluster msg) {
        super(msg);
    }

}
