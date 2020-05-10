package org.coral.jroutine;

import java.util.concurrent.atomic.AtomicInteger;

import org.coral.jroutine.exception.IllegalTaskStateException;
import org.coral.jroutine.observer.Observable;

/**
 * Coroutine, which can be regarded as a lightweight thread, scheduled in the
 * application layer, supporting suspend, resume and stop.
 * 
 * @author lihao
 * @date 2020-04-29
 */
public class Task extends Observable<TaskState> implements Runnable {

    private final static AtomicInteger idSource = new AtomicInteger(0);
    private final static String DEFAULT_TASK_PREFIX_NAME = "DEFAULT-TASK-";
    private final static int MIN_PRIORITY = 1;
    private final static int DEFAULT_PRIORITY = 5;
    private final static int MAX_PRIORITY = 10;

    private int id;
    private String name;
    private int priority;
    private Runnable target;
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
        this.setName(name);
        this.target = target;
        this.setPriority(priority);
        this.id = idSource.getAndIncrement();
    }

    public synchronized void start() {
        if (status != TaskState.NEW) {
            throw new IllegalTaskStateException();
        }
    }

    public final void run() {
        target.run();
    }

    public void suspend() {
        
    }

    public void resume() {
        
    }

    public void sleep(long millis) {

    }

    public void join(long millis) {

    }

    public void join() {

    }

    public void stop() {

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

}
