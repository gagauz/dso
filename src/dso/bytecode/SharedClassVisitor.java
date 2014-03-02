package dso.bytecode;

import dso.annotation.Shared;
import org.objectweb.asm.*;

public class SharedClassVisitor extends ClassAdapter implements Opcodes {

    private String owner;

    private boolean shared;

    public SharedClassVisitor(final ClassVisitor cv) {
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
        if (!shared || mv == null) {
            return mv;
        }
        return new FinallyAdapter(mv, access, name, desc);
    }
}
