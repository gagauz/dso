package dso.cluster;

import dso.utils.EnvParam;

public class DSOPort implements EnvParam<Integer> {

    public static final String ENV_NAME = "DSO_PORT";

    private static int value = 9999;

    static {
        String port = System.getProperty(ENV_NAME, "9999");
        try {
            value = Integer.parseInt(port);
        } catch (NumberFormatException e) {
        }
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public String getName() {
        return ENV_NAME;
    }

}
