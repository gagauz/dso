package dso.bytecode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class Tools implements Opcodes {
    public static void println(AdviceAdapter adapter, String string) {
        adapter.visitFieldInsn(GETSTATIC,
                "java/lang/System", "err",
                "Ljava/io/PrintStream;");
        adapter.visitLdcInsn(string);
        adapter.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream", "println",
                "(Ljava/lang/String;)V");
    }
}
