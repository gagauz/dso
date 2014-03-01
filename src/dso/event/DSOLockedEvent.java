package dso.event;


public class DSOLockedEvent extends DSOEvent {

    private static final long serialVersionUID = 4057717399465174080L;

    @Override
    public boolean locked() {
        return true;
    }
}
