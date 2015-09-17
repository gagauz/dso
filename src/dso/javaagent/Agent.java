package dso.javaagent;

import dso.DSO;
import dso.annotation.Locked;
import dso.annotation.Shared;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {
    public static void premain(String args, Instrumentation inst) {
        System.out.println("*********************************");
        System.out.println(" Hello I'm javaagent in Action! ");
        System.out.println("*********************************");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
                    throws IllegalClassFormatException {
                //                if (className.startsWith("dso/")) {
                //                    System.out.println("** ! Do not transform class " + className);
                //                    return classfileBuffer;
                //                }
                System.out.println("** Tryind to transform class " + className);
                ClassPool pool = ClassPool.getDefault();
                try {
                    CtClass plasticClass = pool.get(className.replace('/', '.'));
                    if (plasticClass.hasAnnotation(Shared.class)) {
                        instrummentLocks(plasticClass);
                        instrummentFields(plasticClass);
                        return plasticClass.toBytecode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                return classfileBuffer;
            }
        });
    }

    private static void instrummentLocks(CtClass plasticClass) {
        for (CtMethod method : plasticClass.getDeclaredMethods()) {
            if (method.hasAnnotation(Locked.class)) {
                try {
                    if ((method.getModifiers() & Modifier.SYNCHRONIZED) == 0) {
                        method.setModifiers(method.getModifiers() | Modifier.SYNCHRONIZED);
                    }
                    String methodSignature = method.getSignature();
                    method.insertBefore(DSO.class.getName() + ".lock(this, \"" + methodSignature + "\");");
                    method.insertAfter(DSO.class.getName() + ".unlock(this, \"" + methodSignature + "\");", true);
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void instrummentFields(CtClass plasticClass) {
        try {
            plasticClass.instrument(new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    try {
                        CtField field = f.getField();
                        if (f.isWriter()) {
                            if ((field.getModifiers() & Modifier.TRANSIENT) == 0) {
                                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                                    String code = DSO.class.getName() + ".checkFieldAccessBegin(this, \"" + f.getFieldName() + "\");";
                                    code += "try {$0." + f.getFieldName() + "=$1;";
                                    code += "} finally {" + DSO.class.getName() + ".checkFieldAccessEnd(this, \"" + f.getFieldName() + "\");}";
                                    f.replace(code);
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
