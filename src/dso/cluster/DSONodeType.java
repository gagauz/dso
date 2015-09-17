package dso.cluster;

import dso.utils.EnvParam;

public enum DSONodeType implements EnvParam<DSONodeType> {

    ALWAYS,
    AUTO,
    NEVER;

    public static final String ENV_NAME = "DSO_MASTER";
    public static DSONodeType value = AUTO;

    private boolean current;

    DSONodeType() {
        String env = System.getProperty(getName());
        current = name().equalsIgnoreCase(env);
        check(this);
    }

    private void check(DSONodeType type) {
        if (type.current) {
            value = type;
        }
    }

    @Override
    public DSONodeType get() {
        return value;
    }

    public boolean isCurrent() {
        return current;
    }

    @Override
    public String getName() {
        return ENV_NAME;
    }
}
