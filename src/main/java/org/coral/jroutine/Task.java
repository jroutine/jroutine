package org.coral.jroutine;

import java.util.concurrent.atomic.AtomicInteger;

import org.coral.jroutine.exception.IllegalTaskStateException;
import org.coral.jroutine.exception.NonEnhancedClassException;
import org.coral.jroutine.observer.Observable;
import org.coral.jroutine.weave.OperandStackRecoder;

/**
 * Coroutine, which can be regarded as a lightweight thread, scheduled in the
 * application layer, supporting suspend, resume and stop.
 * 
 * @author lihao
 * @date 2020-04-29
 */
public class Task extends Observable<TaskState> implements Runnable, Comparable<Task> {

    private final static AtomicInteger idSource = new AtomicInteger(0);
    private final static String DEFAULT_TASK_PREFIX_NAME = "DEFAULT-TASK-";
    private final static int MIN_PRIORITY = 1;
    private final static int DEFAULT_PRIORITY = 5;
    private final static int MAX_PRIORITY = 10;

    private int id;
    private String name;
    private int priority;
    // enhanced class
    private Runnable target;
    // each task needs to hold an operand stack recorder, to record the execution
    // data of the current task.
    private OperandStackRecoder recorder;

    private volatile TaskState status = TaskState.NEW;

    public Task(Runnable target) {
        this(DEFAULT_TASK_PREFIX_NAME, target, DEFAULT_PRIORITY);
    }

    public Task(String name, Runnable target) {
        this(name, target, DEFAULT_PRIORITY);
    }

    public Task(Runnable target, int priority) {
        this(DEFAULT_TASK_PREFIX_NAME, target, priority);
    }

    public Task(String name, Runnable target, int priority) {
        this.target = target;
        this.recorder = new OperandStackRecoder(target);
        this.id = idSource.getAndIncrement();

        this.setName(name);
        this.setPriority(priority);
    }

    public final void run() {
        if (status != TaskState.NEW) {
            throw new IllegalTaskStateException();
        }

        if (!(target instanceof Jroutine)) {
            throw new NonEnhancedClassException();
        }

        try {
            setStatus(TaskState.RUNNABLE);

            OperandStackRecoder.set(this.recorder);

            target.run();

            // TODO how to judge whether the task has been completed

        } catch (Exception e) {
            setStatus(TaskState.TERMINATED);
            throw e;
        } finally {
            // OperandStackRecoder.clear();
        }
    }

    public synchronized void suspend() {
        if (status != TaskState.RUNNABLE) {
            throw new IllegalTaskStateException();
        }
        setStatus(TaskState.SUSPENDING);

        recorder.suspend();
    }

    public void resume() {
        if (status != TaskState.SUSPENDING) {
            throw new IllegalTaskStateException();
        }
        setStatus(TaskState.RUNNABLE);

        target.run();
    }

    public void stop() {
        if (status == TaskState.NEW) {
            throw new IllegalTaskStateException();
        }
        setStatus(TaskState.TERMINATED);

        recorder.suspend();
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        if (status != TaskState.NEW) {
            throw new IllegalTaskStateException();
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPriority(int priority) {
        if (priority > MAX_PRIORITY || priority < MIN_PRIORITY) {
            throw new IllegalArgumentException();
        }
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Task t) {
        return this.priority >= t.priority ? 1 : -1;
    }

    protected void setStatus(TaskState status) {
        if (this.status == TaskState.TERMINATED) {
            throw new IllegalTaskStateException();
        }
        this.status = status;

        notifyObservers(status);
    }

}
