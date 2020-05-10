package org.coral.jroutine.weave;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;

import junit.framework.TestCase;

public class AsmTransformerTest extends TestCase {

    private static final String LOOP = "/org/coral/jroutine/weave/rewrite/Loop.class";

    public void testTracer() throws IOException {
        byte[] bytes = new AsmClassTransformer().transform(new ClassReader(getClass().getResourceAsStream(LOOP)));

        File file = new File("D:\\documents\\work\\eclipse-workspace\\jroutine\\target\\Loop.class");
        if (file.exists()) {
            file.delete();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(bytes);
        bos.close();

        assertNotNull(bytes);
    }
}
