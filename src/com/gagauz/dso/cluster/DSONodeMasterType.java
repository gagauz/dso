package com.gagauz.dso.cluster;

import com.gagauz.dso.utils.EnvParam;

public enum DSONodeMasterType implements EnvParam<DSONodeMasterType> {

    ALWAYS,
    AUTO,
    NEVER;

    public static final String ENV_NAME = "DSO_MASTER";
    public static DSONodeMasterType value = AUTO;

    private boolean current;

    DSONodeMasterType() {
        String env = System.getProperty(getName());
        current = name().equalsIgnoreCase(env);
        check(this);
    }

    private void check(DSONodeMasterType type) {
        if (type.current) {
            value = type;
        }
    }

    @Override
    public DSONodeMasterType get() {
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
