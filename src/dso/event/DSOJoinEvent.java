package dso.event;

import dso.DSOEventCallback;
import dso.thread.DSOClient;

public class DSOJoinEvent extends DSOEvent {

    private static final long serialVersionUID = 1626701315320976113L;

    private final transient DSOClient dsoClient;

    private String name;

    public DSOJoinEvent(DSOClient dsoClient) {
        this.dsoClient = dsoClient;
    }

    @Override
    public DSOEventCallback callback() {
        return new DSOEventCallback() {
            @Override
            public void callOn(DSOEvent event) {
                System.out.println("Execute join callback. Set name " + ((DSOJoinEvent) event).getName());
                dsoClient.setName(((DSOJoinEvent) event).getName());
            }
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
