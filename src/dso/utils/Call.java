package dso.utils;

public interface Call<P, V> {
    V call(P p);
}
