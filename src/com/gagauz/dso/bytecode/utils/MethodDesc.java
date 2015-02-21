package com.gagauz.dso.bytecode.utils;

import org.objectweb.asm.MethodVisitor;

public class MethodDesc {

    private MethodVisitor mv;
    private String ownerClass;
    private String name;
    private String params;
    private String signature;
    private String[] exceptions;

    public MethodDesc(MethodVisitor mv, String owner, int access, String name, String desc, String signature, String[] exceptions) {
        this.mv = mv;
        this.ownerClass = owner;
        this.name = name;
        this.params = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    public MethodVisitor getMv() {
        return mv;
    }

    public String getOwnerClass() {
        return ownerClass;
    }

    public String getName() {
        return name;
    }

    public String getParams() {
        return params;
    }

    public String getSignature() {
        return signature;
    }

    public String[] getExceptions() {
        return exceptions;
    }

}
