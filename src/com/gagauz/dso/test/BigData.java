package com.gagauz.dso.test;

import com.gagauz.dso.annotation.Locked;
import com.gagauz.dso.annotation.SharedObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SharedObject
public class BigData implements Serializable {

    private static int counter = 1;

    private static final long serialVersionUID = -5025247388620814377L;

    private final Integer id;
    private String name;
    private BigData parent;
    private List<BigData> children = new ArrayList<BigData>();

    public BigData() {
        id = ++counter;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        synchronized (this) {
            this.name = n;
        }
    }

    public BigData getParent() {
        return parent;
    }

    @Locked
    public void setParent(BigData newParent) {
        if (this == newParent) {
            throw new IllegalStateException();
        }
        if (null == newParent && parent != null) {
            parent.children.remove(this);
        }
        parent = newParent;
        if (null != parent) {
            parent.children.add(this);
        }
    }

    public List<BigData> getChildren() {
        return children;
    }

    @Locked
    public void setChildren(List<BigData> children) {
        try {
            this.children = children;
        } finally {
            System.out.println("1");
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null != obj && obj instanceof BigData) {
            return ((BigData) obj).hashCode() == hashCode();
        }
        return this == obj;
    }

    @Override
    public String toString() {
        // StringBuilder sb = new StringBuilder("BigData <")
        // .append("id=")
        // .append(hashCode()).append(", ")
        // .append("name=").append(name).append(", ");
        // if (null != parent) {
        // sb.append("parent=").append(parent.hashCode()).append(", ");
        // }
        // sb.append("children=").append(children).append(">\n");
        // return sb.toString();
        return this.getClass().getName() + "#" + hashCode();
    }
}
