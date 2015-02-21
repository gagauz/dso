package com.gagauz.dso.utils;

public class Pair<K, V> {
    private final K k;
    private final V v;

    public Pair(K key, V value) {
        k = key;
        v = value;
    }
    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }

    @Override
    public int hashCode() {
        return k == null ? 0 : k.hashCode();
    }
}
