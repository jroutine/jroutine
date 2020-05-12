/*    */
package org.coral.jroutine.weave.rewrite;

import java.io.PrintStream;
import org.coral.jroutine.weave.OperandStackRecoder;

/*    */ public class LoopDecompile implements Runnable {
    public void run() {
        PrintStream var10000;
        String s;
        boolean var10001;
        Object o;
        OperandStackRecoder recorder;
        label40: {
            Object var10002;
            String var4;
            label44: {
                if ((recorder = OperandStackRecoder.get()) != null && recorder.isRestoring) {
                    switch (recorder.popInt()) {
                    case 0:
//                        this = (Loop) recorder.popObject();
                        break;
                    case 1:
                        o = (Object) recorder.popObject();
                        s = (String) recorder.popObject();
//                        this = (Loop) recorder.popObject();
                        var10000 = (PrintStream) recorder.popObject();
                        var4 = (String) recorder.popReference();
                        var10002 = null;
                        break label44;
                    case 2:
                        o = (Object) recorder.popObject();
                        s = (String) recorder.popObject();
//                        this = (Loop) recorder.popObject();
                        var10000 = (PrintStream) recorder.popReference();
                        var10001 = false;
                        /*    */ break label40;
                    /*    */ }
                    /*    */ }
                /*    */
                /* 7 */ test();
                if (recorder != null && recorder.isCapturing) {
                    recorder.pushReference(this);
                    recorder.pushObject(this);
                    recorder.pushInt(0);
                    return;
                }
                /* 8 */ s = new String();
                /* 9 */ o = new Object();
                /* 10 */ var10000 = System.out;
                var4 = s;
                var10002 = o;
            }
            var10001 = var4.equals(var10002);
            if (recorder != null && recorder.isCapturing) {
                recorder.pushObject(var10000);
                recorder.pushReference(this);
                recorder.pushObject(this);
                recorder.pushObject(s);
                recorder.pushObject(o);
                recorder.pushInt(1);
                return;
            }
        }
        var10000.println(var10001);
        if (recorder != null && recorder.isCapturing) {
            recorder.pushReference(this);
            recorder.pushObject(this);
            recorder.pushObject(s);
            recorder.pushObject(o);
            recorder.pushInt(2);
        }
        /* 11 */ }

    /*    */
    /*    */
    /*    */
    /*    */ private static void test() {
        /* 16 */ }
    /*    */ }
