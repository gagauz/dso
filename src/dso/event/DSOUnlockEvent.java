package dso.event;

public class DSOUnlockEvent extends DSOEvent {

    private static final long serialVersionUID = -301137498978274273L;
    private final long threadId;
    private final String className;
    private final String methodName;
    private final int objectHash;

    public DSOUnlockEvent(long id, Object object, String name) {
        this.threadId = id;
        this.className = object.getClass().getName();
        this.methodName = name;
        this.objectHash = object.hashCode();
    }

    public long getThreadId() {
        return threadId;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getObjectHash() {
        return objectHash;
    }
}
