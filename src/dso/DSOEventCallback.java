package dso;

import dso.event.DSOEvent;

abstract public class DSOEventCallback {

    public static final DSOEventCallback VOID = new DSOEventCallback() {

        @Override
        public void callOn(DSOEvent event) {
            //void
            System.out.println("Call callback on " + event);
        }
    };

    public abstract void callOn(DSOEvent event);
}
