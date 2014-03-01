package dso.event;

public class DSOLockEvent extends DSOEvent {

    private static final long serialVersionUID = -301137498978274273L;
    private long threadId;
    private String className;
    private String methodName;
    private int objectHash;
    private transient int serverThreadHashCode;

    public DSOLockEvent(long id, Object object, String name) {
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

    public int getServerThreadHashCode() {
        return serverThreadHashCode;
    }

    public void setServerThreadHashCode(int hashCode) {
        this.serverThreadHashCode = hashCode;
    }

}
