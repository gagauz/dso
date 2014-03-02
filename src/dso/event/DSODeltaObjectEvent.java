package dso.event;


public class DSODeltaObjectEvent extends DSOEvent {
    private static final long serialVersionUID = -5578160500397889505L;
    private final Object object;

    public DSODeltaObjectEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
