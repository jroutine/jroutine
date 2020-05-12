package org.coral.jroutine.schedule;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.coral.jroutine.AbstractLifecycle;
import org.coral.jroutine.Task;
import org.coral.jroutine.exception.LifecycleException;

/**
 * implementation of Executor based on priority queue.
 * 
 * @author lihao
 * @date 2020-05-12
 */
public class PriorityExecutor extends AbstractLifecycle implements Executor<Task> {

    private PriorityBlockingQueue<Runnable> queue;
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void initInternal() throws LifecycleException {
        queue = new PriorityBlockingQueue<Runnable>(1000);
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.HOURS, queue,
                new NamedThreadFactory("EXECUTOR", false));
    }

    @Override
    protected void startInternal() throws LifecycleException {

    }

    @Override
    protected void stopInternal() throws LifecycleException {
        threadPoolExecutor.shutdown();
    }

    @Override
    public void execute(Task t) {
        threadPoolExecutor.execute(t);
    }

}
