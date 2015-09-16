package dso.javaagent;

import dso.annotation.Shared;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

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
                if (className.startsWith("dso/")) {
                    System.out.println("** ! Do not transform class " + className);
                    return classfileBuffer;
                }
                System.out.println("** Transform class " + className);

                System.out.println("** Execute DSOClassLoader.main method!");
                ClassPool pool = ClassPool.getDefault();
                try {

                    CtClass strCc = pool.get(className.replace('/', '.'));
                    if (strCc.getAnnotation(Shared.class) != null) {
                        CtClass stringCt = pool.get("java.lang.String");
                        CtMethod m = new CtMethod(stringCt, "toString", new CtClass[0], strCc);
                        strCc.addMethod(m);
                        System.out.println("** Done");
                        return strCc.toBytecode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                return classfileBuffer;
            }
        });
    }
}
