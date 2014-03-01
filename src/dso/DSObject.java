package dso;

import java.io.Serializable;

abstract public class DSObject implements Serializable {

    private static final long serialVersionUID = 1L;

    public DSObject() {
        //DSO.registerObject(this);
        //        for (Method m : getClass().getMethods()) {
        //            System.out.println(m.toGenericString());
        //        }
    }

    public String __hash() {
        return getClass().getCanonicalName() + "@" + hashCode();
    }
}
