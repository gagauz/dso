package com.gagauz.dso.bytecode;

import com.gagauz.dso.annotation.SharedObject;

import org.objectweb.asm.*;

public class SharedClassVisitor extends ClassAdapter implements Opcodes {

    private boolean sharedObject;

    public SharedClassVisitor(final ClassVisitor cv) {
        super(cv);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
        AnnotationVisitor av = super.visitAnnotation(arg0, arg1);
        sharedObject = Type.getType(arg0).getClassName().equals(SharedObject.class.getName());
        return av;
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
                                   final Object value) {
        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
        System.out.println(name + " " + desc);
        if (!sharedObject) {
            System.out.println(name + " is not shared. Skip.");
        }
        return fv;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
                                     final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (!sharedObject || mv == null) {
            return mv;
        }
        return new FinallyAdapter(mv, access, name, desc);
    }
}
