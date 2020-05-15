package org.coral.jroutine.schedule;

import java.util.ArrayList;
import java.util.List;

import org.coral.jroutine.AbstractLifecycle;
import org.coral.jroutine.exception.LifecycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * for internal monitoring.
 * 
 * @author lihao
 * @date 2020-05-15
 */
public class WatchDog extends AbstractLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(WatchDog.class);
    private static final WatchDog WATCH_DOG = new WatchDog();

    private List<Monitor> monitors;
    private Thread daemon;

    public WatchDog() {
        init();
    }

    public static WatchDog me() {
        return WATCH_DOG;
    }

    public void addMonitor(Monitor monitor) {
        monitors.add(monitor);
    }

    public void remove(Monitor monitor) {
        monitors.remove(monitor);
    }

    @Override
    protected void initInternal() throws LifecycleException {
        monitors = new ArrayList<Monitor>();

        daemon = new Thread(new Runnable() {

            @Override
            public void run() {

                synchronized (monitors) {
                    for (int i = 0; i < monitors.size(); i++) {
                        // just print it out
                        logger.info(monitors.get(i).status());
                    }
                }

            }
        }, "JROUTINE-WATCHDOG-T1");
        daemon.setDaemon(true);
        daemon.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    protected void startInternal() throws LifecycleException {
        daemon.start();
    }

    @Override
    protected void stopInternal() throws LifecycleException {
        monitors = new ArrayList<Monitor>();
        daemon = null;
    }
}
