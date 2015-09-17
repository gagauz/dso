package dso.bytecode.impl;

import dso.bytecode.api.Getter;
import dso.bytecode.api.PlasticClass;
import dso.bytecode.api.Setter;
import javassist.CtClass;

public class PlasticClassImpl implements PlasticClass {

    private final CtClass ctClass;

    public PlasticClassImpl(CtClass ctClass) {
        this.ctClass = ctClass;
    }

    @Override
    public void wrapFieldGet(String fieldName, Getter getter) {

    }

    @Override
    public void wrapFieldSet(String fieldName, Setter setter) {

    }

    @Override
    public void wrapMethodCall(String methodName, Setter setter) {

    }

}
