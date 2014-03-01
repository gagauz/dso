package dso.event;


public class DSOShareObjectEvent extends DSOEvent {
    private static final long serialVersionUID = -5578160500397889505L;
    private final Object object;

    public DSOShareObjectEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
