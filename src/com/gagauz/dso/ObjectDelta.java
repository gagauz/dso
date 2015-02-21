package com.gagauz.dso;

import com.gagauz.dso.utils.Pair;

import java.util.List;

public class ObjectDelta {
    private final Pair<Integer, Object>[] data;

    public ObjectDelta(List<Pair<Integer, Object>> data) {
        this.data = data.toArray(new Pair[data.size()]);
    }

    public Object getNewValue(String field) {
        int hash = field.hashCode();
        for (Pair<Integer, Object> pair : data) {
            if (pair.getKey() == hash) {
                return pair.getValue();
            }
        }
        return null;
    }
}
