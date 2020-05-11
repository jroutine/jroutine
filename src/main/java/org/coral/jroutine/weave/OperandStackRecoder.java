package org.coral.jroutine.weave;

/**
 * OperandStackRecoder
 * 
 * @author lihao
 * @date 2020-05-10
 */
public class OperandStackRecoder extends OperandStack {

    private static final long serialVersionUID = 2957061267209401968L;

    private static ThreadLocal<OperandStackRecoder> threadMap = new ThreadLocal<OperandStackRecoder>();

    public volatile boolean isRestoring;
    public volatile boolean isCapturing;

    public OperandStackRecoder(Runnable pRunnable) {
        super(pRunnable);
    }

    public static OperandStackRecoder get() {
        return threadMap.get();
    }

    public static void set(OperandStackRecoder recorder) {
        if (recorder == null) {
            throw new IllegalArgumentException();
        }
        threadMap.set(recorder);
    }

    public static void clear() {
        threadMap.remove();
    }

    public void suspend() {
        isCapturing = !isRestoring;
        isRestoring = false;
    }

}
