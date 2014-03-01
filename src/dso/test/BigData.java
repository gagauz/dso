package dso.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BigData implements Serializable {

    private static int counter = 1;

    private static final long serialVersionUID = -5025247388620814377L;

    private Integer id;
    private String name;
    private BigData parent;
    private List<BigData> children = new ArrayList<BigData>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigData getParent() {
        return parent;
    }

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

    public void setChildren(List<BigData> children) {
        this.children = children;
    }

    @Override
    public int hashCode() {
        if (null == id) {
            id = counter++;
        }
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
        StringBuilder sb = new StringBuilder("BigData <")
                .append("id=")
                .append(hashCode()).append(", ")
                .append("name=").append(name).append(", ");
        if (null != parent) {
            sb.append("parent=").append(parent.hashCode()).append(", ");
        }
        sb.append("children=").append(children).append(">\n");
        return sb.toString();
    }
}
