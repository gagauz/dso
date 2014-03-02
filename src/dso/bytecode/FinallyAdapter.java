package dso.bytecode;

import dso.annotation.Locked;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class FinallyAdapter extends AdviceAdapter {
    private String name;
    private Label startFinally = new Label();
    private boolean locked;

    public FinallyAdapter(MethodVisitor mv, int acc, String name, String desc) {
        super(mv, acc, name, desc);
        this.name = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
        AnnotationVisitor av = mv.visitAnnotation(arg0, arg1);
        this.locked = Type.getType(arg0).getClassName().equals(Locked.class.getName());
        return av;
    }

    @Override
    protected void onMethodEnter() {
        if (locked) {
            super.visitFieldInsn(GETSTATIC,
                    "java/lang/System", "err",
                    "Ljava/io/PrintStream;");
            super.visitLdcInsn("** LOCKED ENTER " + name);
            super.visitMethodInsn(INVOKEVIRTUAL,
                    "java/io/PrintStream", "println",
                    "(Ljava/lang/String;)V");
        }
    }

    @Override
    public void visitCode() {
        super.visitCode();
        if (locked)
            mv.visitLabel(startFinally);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        if (locked) {
            Label endFinally = new Label();
            mv.visitTryCatchBlock(startFinally,
                    endFinally, endFinally, null);
            mv.visitLabel(endFinally);
            onFinally(ATHROW);
            mv.visitInsn(ATHROW);
        }
        mv.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (locked) {
            if (opcode != ATHROW) {
                onFinally(opcode);
            }
        } else {
            super.onMethodExit(opcode);
        }
    }

    @Override
    public void visitEnd() {
        locked = false;
        super.visitEnd();
    }

    private void onFinally(int opcode) {
        mv.visitFieldInsn(GETSTATIC,
                "java/lang/System", "err",
                "Ljava/io/PrintStream;");
        mv.visitLdcInsn("** LOCK EXIT " + name);
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V");
    }

}
