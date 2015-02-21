package com.gagauz.dso.bytecode;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.ArrayList;
import java.util.List;

public class FinallyAdapter extends AdviceAdapter {

    private static class VarInsn {
        int opcode;
        int index;

        VarInsn(int o, int i) {
            this.opcode = o;
            this.index = i;
        }
    }

    private static class FieldInsn {
        int opcode;
        String oType;
        String fName;
        String fType;

        FieldInsn(int o, String oT, String fN, String fT) {
            this.opcode = o;
            this.oType = oT;
            this.fName = fN;
            this.fType = fT;
        }
    }

    private String name;
    private boolean locked;
    private Label l0;
    private List<Label> monitorEnter = new ArrayList<Label>();
    private List<Label> monitorExit = new ArrayList<Label>();
    private VarInsn lastASTORE;
    private String lastVAR;

    public FinallyAdapter(MethodVisitor mv, int acc, String name, String desc) {
        super(mv, acc, name, desc);
        this.name = name;
        this.locked = (acc & ACC_SYNCHRONIZED) != 0;
        if (locked) {
            l0 = new Label();
        }
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        System.out.format("visitLocalVariable %s %s %s %s\n", name, desc, signature, index);
        lastVAR = String.format("** %s %s %s %s\n", name, desc, signature, index);
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    protected void onMethodEnter() {
        System.out.println("Enter method " + name);
        if (locked) {
            Tools.println(this, "** SYNCHRONIZED METHOD ENTER : LOCK START " + name);
            mv.visitLabel(l0);
        }
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        if (ALOAD == opcode) {

        } else if (ASTORE == opcode) {
            lastASTORE = new VarInsn(opcode, var);
        }
        System.out.format("visitVarInsn %s %s\n", opcode, var);
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitInsn(final int opcode) {
        if (opcode == MONITORENTER) {

            Label l1 = new Label();
            Label l2 = new Label();
            monitorEnter.add(l1);
            monitorExit.add(l2);
            monitorExit.add(l2);
            //mv.visitTryCatchBlock(l1, l2, l2, null);
            System.out.format("** MONITORENTER (on local variable %s) >> {\n", lastVAR);
            super.visitInsn(opcode);
            //mv.visitLabel(l1);

            Tools.println(this, "** MONITORENTER : lock on var[" + lastASTORE.index + "] method " + name);
        } else if (opcode == MONITOREXIT) {

            if (!monitorExit.isEmpty()) {
                Label l2 = monitorExit.remove(monitorExit.size() - 1);
                //  mv.visitLabel(l2);
                Tools.println(this, "** MONITOREXIT : LOCK method " + name);
                //                mv.visitFrame(F_SAME, 0, null, 0, null);
                //                mv.visitInsn(ATHROW);
            }
            System.out.format("** } >> MONITOREXIT\n");
            super.visitInsn(opcode);
        } else {
            super.visitInsn(opcode);
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        if (locked) {
            Label l1 = new Label();
            mv.visitTryCatchBlock(l0, l1, l1, null);
            mv.visitLabel(l1);
            onFinally();
            mv.visitInsn(ATHROW);
        }
        mv.visitMaxs(maxStack, maxLocals);
    }

    @Override
    protected void onMethodExit(int opcode) {
        System.out.println("Exit method " + name + " " + opcode);
        if (locked) {
            if (opcode != ATHROW) {
                onFinally();
            }
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
