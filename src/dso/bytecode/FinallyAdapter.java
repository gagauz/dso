package dso.bytecode;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class FinallyAdapter extends AdviceAdapter {
    private String name;
    private Label startFinally = new Label();
    private boolean locked;

    public FinallyAdapter(MethodVisitor mv, int acc, String name, String desc) {
        super(mv, acc, name, desc);
        this.name = name;
        this.locked = (acc & ACC_SYNCHRONIZED) != 0;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
        AnnotationVisitor av = mv.visitAnnotation(arg0, arg1);
        return av;
    }

    @Override
    protected void onMethodEnter() {
        if (locked) {
            Tools.println(this, "** SYNCHRONIZED METHOD ENTER : LOCK START " + name);
        }
    }

    @Override
    public void visitCode() {
        super.visitCode();
        if (locked) {
            mv.visitLabel(startFinally);
        }
    }

    @Override
    public void visitInsn(final int opcode) {
        if (opcode == MONITORENTER) {
            Tools.println(this, "** SYNCHRONIZED BLOCK ENTER : LOCK START " + name);
        } else if (opcode == MONITOREXIT) {
            Tools.println(this, "** SYNCHRONIZED BLOCK EXIT : LOCK RELEASE " + name);
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        if (locked) {
            Label endFinally = new Label();
            mv.visitTryCatchBlock(startFinally,
                    endFinally, endFinally, null);
            mv.visitLabel(endFinally);
            onFinally();
            mv.visitInsn(ATHROW);
        }
        mv.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (locked) {
            if (opcode != ATHROW) {
                onFinally();
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

    private void onFinally() {
        Tools.println(this, "** SYNCHRONIZED METHOD EXIT : LOCK RELEASE " + name);
    }
}
