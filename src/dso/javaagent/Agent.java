package dso.javaagent;

import dso.DSO;
import dso.annotation.Locked;
import dso.annotation.Root;
import dso.annotation.Shared;
import dso.object.DSManager;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {

    private static final String DSO_MANAGER_CLASS = DSManager.class.getName();
    private static final String DSO_CLASS = DSO.class.getName();

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
                        instrummentRoots(plasticClass);
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
                        if ((field.getModifiers() & Modifier.TRANSIENT) == 0) {
                            if ((field.getModifiers() & Modifier.STATIC) == 0) {
                                if (f.isWriter()) {

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

    private static void addDSOManagerField(CtClass ctClass) throws NotFoundException, CannotCompileException {
        try {
            CtField dsoField = ctClass.getField("__dso_manager");
        } catch (NotFoundException e) {
            CtClass dsoClass = ClassPool.getDefault().get("dso.object.DSObjectManager");
            CtField dsoField = new CtField(dsoClass, "__dso_manager", ctClass);
            ctClass.addField(dsoField);
        }

    }

    private static void instrummentRoots(CtClass plasticClass) {
        try {

            CtField field = new CtField(CtClass.booleanType, "__dso_instrummented", plasticClass);

            plasticClass.instrument(new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    try {
                        System.out.println("Instrument field " + f.getFieldName());
                        CtField field = f.getField();
                        if (field.hasAnnotation(Root.class)) {
                            if (field.getType().isArray()) {
                                throw new IllegalStateException("Root field cannot be array!");
                            }
                            if (field.getType().isAnnotation()) {
                                throw new IllegalStateException("Root field cannot be annotation!");
                            }
                            if (field.getType().isEnum()) {
                                throw new IllegalStateException("Root field cannot be enum!");
                            }
                            if (field.getType().isInterface()) {
                                throw new IllegalStateException("Root field cannot be interface!");
                            }
                            if (field.getType().isPrimitive()) {
                                throw new IllegalStateException("Root field cannot be primitive!");
                            }
                            //                            if (!field.getType().hasAnnotation(Shared.class)) {
                            //                                throw new IllegalStateException("The type of Root field should be marked with Shared annotation!");
                            //                            }
                            System.out.println("Field has Root annotation");
                            final String rootName = getRootUID(field);
                            instrummentRootFields(field);
                            if (f.isWriter()) {
                                f.replace(DSO_CLASS + ".putClusteredObject(\"" + rootName + "\", $1);");
                            }
                            if (f.isReader()) {
                                f.replace("$_ = (" + f.getField().getType().getName() + ") " + DSO_CLASS + ".getClusteredObject(\"" + rootName + "\");");
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

    private static String getRootUID(CtField rootField) {
        return rootField.getDeclaringClass().getName() + ':' + rootField.getName();
    }

    private static void instrummentRootFields(final CtField rootField) {
        try {
            CtClass rootClass = rootField.getType();
            rootClass.instrument(new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    try {
                        System.out.println("Instrument field " + f.getFieldName());
                        CtField field = f.getField();
                        if (field.hasAnnotation(Root.class)) {
                            throw new IllegalStateException("Root fields cannot be inside root objects!");
                        }
                        System.out.println("Field has Root annotation");
                        final String rootName = f.getClassName() + '.' + field.getName();
                        CtClass fieldClass = f.getField().getType();
                        if (f.isWriter()) {
                            String code = DSO_CLASS + ".checkInsideLock();";
                            code += "synchronized(this) {";
                            code += DSO_CLASS + ".recordFieldChange(" + getRootUID(rootField) + ");";
                            code += "}";
                            f.replace(code);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
