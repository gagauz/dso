package dso.test;

import dso.annotation.Locked;
import dso.annotation.Shared;
import dso.bytecode.SharedClassVisitor;
import org.objectweb.asm.*;

import java.io.InputStream;

public class TestAsm extends ClassLoader implements Opcodes {

    @Override
    protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        if (name.startsWith("java.") || name.startsWith("dso.DSO")) {
            System.err.println("Adapt: loading class '" + name + "' without on the fly adaptation");
            return super.loadClass(name, resolve);
        } else {
            System.err.println("Adapt: loading class '" + name + "' with on the fly adaptation");
        }

        // gets an input stream to read the bytecode of the class
        String resource = name.replace('.', '/') + ".class";
        InputStream is = getResourceAsStream(resource);
        byte[] b;

        // adapts the class on the fly
        try {
            ClassReader reader = new ClassReader(is);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor visitor = new SharedClassVisitor(writer);
            reader.accept(visitor, ClassReader.EXPAND_FRAMES);
            b = writer.toByteArray();
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        }

        // returns the adapted class
        return defineClass(name, b, 0, b.length);
    }

    public static void main(String[] args) {
        try {

            ClassLoader loader = new TestAsm();

            Thread.currentThread().setContextClassLoader(loader);

            Class<?> bdClass = loader.loadClass(BigData.class.getCanonicalName());

            Object b = bdClass.newInstance();

            bdClass.getMethod("getName").invoke(b);
            bdClass.getMethod("setName", String.class).invoke(b, "");
            System.out.println(bdClass.getMethod("getName").invoke(b).toString());
            bdClass.getMethod("toString").invoke(b);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

class TraceFieldClassAdapter extends ClassAdapter implements Opcodes {

    private String owner;

    private boolean shared;

    public TraceFieldClassAdapter(final ClassVisitor cv) {
        super(cv);
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature,
                      final String superName, final String[] interfaces) {
        owner = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
        AnnotationVisitor av = super.visitAnnotation(arg0, arg1);
        shared = Type.getType(arg0).getClassName().equals(Shared.class.getName());
        return av;
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
                                   final Object value) {
        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
        System.out.println(name + " " + desc);
        if (!shared) {
            System.out.println(name + " is not shared. Skip.");
        }
        // if ((access & (ACC_STATIC | ACC_TRANSIENT)) == 0) {
        // Type t = Type.getType(desc);
        // int size = t.getSize();
        //
        // // generates getter method
        // String gName = "__get_" + name;
        // String gDesc = "()" + desc;
        //
        // MethodVisitor getVisitor = cv.visitMethod(ACC_PRIVATE, gName, gDesc,
        // null, null);
        // getVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "err",
        // "Ljava/io/PrintStream;");
        // getVisitor.visitLdcInsn(gName + " called");
        //
        // getVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
        // "println", "(Ljava/lang/String;)V");
        // getVisitor.visitVarInsn(ALOAD, 0);
        // getVisitor.visitFieldInsn(GETFIELD, owner, name, desc);
        // getVisitor.visitInsn(t.getOpcode(IRETURN));
        // getVisitor.visitMaxs(1 + size, 1);
        // getVisitor.visitEnd();
        //
        // // generates setter method
        // String sName = "__set_" + name;
        // String sDesc = "(" + desc + ")V";
        //
        // MethodVisitor setVisitor = cv.visitMethod(ACC_PRIVATE, sName, sDesc,
        // null, null);
        // // setVisitor.visitFieldInsn(GETSTATIC,
        // // "java/lang/System",
        // // "err",
        // // "Ljava/io/PrintStream;");
        // // setVisitor.visitFieldInsn(GETSTATIC,
        // // "dso/DSO",
        // // "lock",
        // // "Ljava/io/PrintStream;");
        //
        // setVisitor.visitVarInsn(ALOAD, 0);
        // setVisitor.visitLdcInsn(sName);
        // setVisitor.visitMethodInsn(INVOKESTATIC, "dso/DSO", "lock",
        // "(Ljava/lang/Object;Ljava/lang/String;)V");
        //
        // setVisitor.visitVarInsn(ALOAD, 0);
        // setVisitor.visitVarInsn(t.getOpcode(ILOAD), 1);
        // setVisitor.visitFieldInsn(PUTFIELD, owner, name, desc);
        //
        // setVisitor.visitVarInsn(ALOAD, 0);
        // setVisitor.visitLdcInsn(sName);
        // setVisitor.visitMethodInsn(INVOKESTATIC, "dso/DSO", "unlock",
        // "(Ljava/lang/Object;Ljava/lang/String;)V");
        //
        // setVisitor.visitInsn(RETURN);
        // setVisitor.visitMaxs(1 + size, 1 + size);
        // setVisitor.visitEnd();
        // }
        return fv;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
                                     final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return mv == null ? null : new LockedAnnotaionAdapter(mv, owner, name);
    }
}

class LockedAnnotaionAdapter extends MethodAdapter implements Opcodes {

    private final String owner;
    private final String name;
    private boolean locked;

    public LockedAnnotaionAdapter(final MethodVisitor mv, final String owner, final String name) {
        super(mv);
        this.owner = owner;
        this.name = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
        AnnotationVisitor av = mv.visitAnnotation(arg0, arg1);
        locked = Type.getType(arg0).getClassName().equals(Locked.class.getName());
        System.out.println(locked);
        return null;
    }

    @Override
    public void visitCode() {
        if (locked)
            System.out.println("-- ++ visit code " + name);
        super.visitCode();
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        // if (owner.equals(this.owner)) {
        // if (opcode == GETFIELD) {
        // // replaces GETFIELD f by INVOKESPECIAL _getf
        // String gDesc = "()" + desc;
        // // visitMethodInsn(INVOKESPECIAL, owner, "__get_" + name,
        // // gDesc);
        // return;
        // } else if (opcode == PUTFIELD) {
        // // replaces PUTFIELD f by INVOKESPECIAL _setf
        // String sDesc = "(" + desc + ")V";
        // // visitMethodInsn(INVOKESPECIAL, owner, "__set_" + name,
        // // sDesc);
        // return;
        // }
        // }
        // if (owner.equals(this.owner))
        // System.out.println(opcode + " " + owner + " " + name + " " + desc +
        // " / " + locked);
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitEnd() {
        locked = false;
        super.visitEnd();
    }

}
