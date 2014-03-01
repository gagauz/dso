package dso.cluster;

import java.util.ArrayList;
import java.util.List;

import dso.utils.EnvParam;

public class DSONodeList implements EnvParam<List<String>> {
    private static List<String> nodes;

    public static final String ENV_NAME = "DSO_NODES";

    static {
        nodes = new ArrayList<String>();
        String env = System.getProperty(ENV_NAME);
        try {
            String[] list = env.split(",");
            for (String str : list) {
                nodes.add(str.trim());
            }
        } catch (Exception e) {
            nodes.add("0.0.0.0");
        }
    }

    @Override
    public List<String> get() {
        return nodes;
    }

    @Override
    public String getName() {
        return ENV_NAME;
    }
}
