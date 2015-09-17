package dso.object;

import dso.DSO;

import java.io.Serializable;
import java.util.List;

public class DSOManager implements Serializable {
    private static final long serialVersionUID = -3597437449286746853L;
    private final long uid;
    private List<DSOChange> list;

    public DSOManager() {
        this.uid = DSO.allocateId();
    }

    public long getUid() {
        return uid;
    }

    public void recordChange(DSOChange change) {
        list.add(change);
    }

    public List<DSOChange> getChanges() {
        return list;
    }

}
