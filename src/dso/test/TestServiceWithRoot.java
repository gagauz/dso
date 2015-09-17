package dso.test;

import dso.DSO;
import dso.annotation.Shared;

@Shared
public class TestServiceWithRoot {

    public static class RootObj {
        private String field;
        private String field2;

        public String get() {
            return field;
        }

        public String get2() {
            return field2;
        }

        public void set(String newValue) {
            DSO.lock(this, "set");
            try {
                field2 = field;
                DSO.recordChange(this, "field2", field);
                field = newValue;
                DSO.recordChange(this, "field", newValue);
            } finally {
                DSO.unlock(this, "set");
            }
        }

        @Override
        public String toString() {
            return "This is Root object with string field = " + field;
        }
    }

    private static final RootObj root = new RootObj();

    public RootObj getRoot() {
        return root;
    }
}
