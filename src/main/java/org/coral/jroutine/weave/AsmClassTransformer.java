package org.coral.jroutine.weave;

import java.io.PrintWriter;
import java.lang.reflect.Field;

import org.coral.jroutine.Configs;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

/**
 * Bytecode transformer via asm.
 * 
 * @author lihao
 * @date 2020-05-08
 */
public class AsmClassTransformer implements ClassTransformer {

    @Override
    public byte[] transform(byte[] classFile) {
        return transform(new ClassReader(classFile));
    }

    public byte[] transform(ClassReader cr) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        // make sure that the transformed bytecode is sane
//        ClassVisitor visitor = new CheckClassAdapter(cw, false);
        ClassVisitor visitor = new CheckClassAdapter(cw);
        if (null != CHECK_DATA_FLOW) {
            try {
                CHECK_DATA_FLOW.set(visitor, Boolean.FALSE);
            } catch (final IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        // prints the classes
        if (Configs.isDebugEnabled()) {
            visitor = new TraceClassVisitor(visitor, new PrintWriter(System.out));
        }
        // enhance bytecode
        visitor = new JroutineClassAdapter(visitor);

        cr.accept(visitor, 0);

        return cw.toByteArray();
    }
    
    final private static Field CHECK_DATA_FLOW;

    static {
        Field checkDataFlow = null;
        try {
            checkDataFlow = CheckClassAdapter.class.getDeclaredField("checkDataFlow");
            checkDataFlow.setAccessible(true);
        } catch (final NoSuchFieldException ex) {
            // Normal, the field is available only since ASM 3.2
        }

        CHECK_DATA_FLOW = checkDataFlow;
    }

}
