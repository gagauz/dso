package dso.bytecode.api;

import javassist.*;

import java.io.IOException;

public class DSOClassLoader extends ClassLoader {

    private ClassPool pool;

    public DSOClassLoader() throws NotFoundException {
        super();
        pool = new ClassPool();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            System.out.println("*** Load class " + name);
            CtClass cc = pool.get(name);
            byte[] b = cc.toBytecode();
            return defineClass(name, b, 0, b.length);
        } catch (NotFoundException | IOException | CannotCompileException e) {
            throw new ClassNotFoundException("Failed to load class " + name, e);
        }
    }

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, SecurityException {
        System.out.println("** Execute DSOClassLoader.main method!");
        ClassPool pool = ClassPool.getDefault();
        CtClass dso = pool.get("dso.DSO");
        CtClass strCc = pool.get("java.lang.String");
        CtField f = new CtField(CtClass.intType, "hiddenValue", strCc);
        f.setModifiers(Modifier.PUBLIC);
        strCc.addField(f);
        strCc.writeFile(".");
        System.out.println("** Done");
    }
}
