package dso.event;

import dso.DSObject;

public class DSOSyncRootEvent extends DSOEvent {
    private static final long serialVersionUID = -5578160500397889505L;
    private final DSObject object;

    public DSOSyncRootEvent(DSObject object) {
        this.object = object;
    }

    public DSObject getObject() {
        return object;
    }
}
