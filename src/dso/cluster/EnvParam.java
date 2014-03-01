package dso.cluster;

public interface EnvParam<V> {
    String getName();

    V get();
}
