package dso.bytecode.api;

public interface PlasticClass {
    void wrapFieldGet(String fieldName, Getter getter);

    void wrapFieldSet(String fieldName, Setter setter);

    void wrapMethodCall(String methodName, Setter setter);
}
