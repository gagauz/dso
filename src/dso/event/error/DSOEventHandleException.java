package dso.event.error;

import dso.event.DSOEvent;

public class DSOEventHandleException extends RuntimeException {
    private static final long serialVersionUID = -1907398494100930809L;
    private final DSOEvent event;

    public DSOEventHandleException(DSOEvent event) {
        this.event = event;
    }

    public DSOEventHandleException(String message, DSOEvent event) {
        super(message);
        this.event = event;

    }

    public DSOEventHandleException(String message, Throwable cause, DSOEvent event) {
        super(message, cause);
        this.event = event;
    }

    public DSOEvent getEvent() {
        return event;
    }
}
