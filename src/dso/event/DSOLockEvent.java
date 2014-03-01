package dso.event;

public class DSOLockEvent extends DSOUnlockEvent {

    private static final long serialVersionUID = -1102989500945048666L;

    private transient int nodeId;

    public DSOLockEvent(long id, Object object, String name) {
        super(id, object, name);
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int hashCode) {
        this.nodeId = hashCode;
    }

}
