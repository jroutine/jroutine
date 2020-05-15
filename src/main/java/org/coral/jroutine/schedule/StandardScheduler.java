package org.coral.jroutine.schedule;

import java.util.concurrent.TimeUnit;

import org.coral.jroutine.AbstractLifecycle;
import org.coral.jroutine.Task;
import org.coral.jroutine.config.Configs;
import org.coral.jroutine.config.LoadBalanceType;
import org.coral.jroutine.exception.LifecycleException;
import org.coral.jroutine.schedule.lb.LoadBalancer;
import org.coral.jroutine.schedule.lb.RoundRobinLoadBalancer;
import org.coral.jroutine.schedule.lb.WeightRoundRobinLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the standard scheduler, assigns executor to the submitted task.
 * 
 * @author lihao
 * @date 2020-05-12
 */
public class StandardScheduler extends AbstractLifecycle implements Scheduler<Task> {

    private static final Logger logger = LoggerFactory.getLogger(StandardScheduler.class);

    private static final long THREAD_KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.HOURS;
    private static final int EXECUTOR_QUEUE_SIZE = 1000;
    private static final LoadBalanceType DEFAULT_LOAD_BALANCER = LoadBalanceType.ROUND_ROBIN;

    private Executor<Task>[] executors;
    private LoadBalancer loadBalancer;

    @Override
    protected void initInternal() throws LifecycleException {
        initExecutors();
        initLoadBalancer();
    }

    @Override
    protected void startInternal() throws LifecycleException {
        for (Executor<Task> executor : executors) {
            executor.start();
        }

        if (Configs.isDebugEnabled()) {
            WatchDog.me().start();
        }
    }

    @Override
    protected void stopInternal() throws LifecycleException {
        for (Executor<Task> executor : executors) {
            executor.stop();
        }

        WatchDog.me().stop();
    }

    @Override
    public void submit(Task task) {
        Executor<Task> executor = selectExecutor();
        executor.execute(task);
    }

    private void initExecutors() {
        int coreSize = Configs.getExecutorsCoreSize() == -1 ? Runtime.getRuntime().availableProcessors()
                : Configs.getExecutorsCoreSize();
        long keepAliveTime = Configs.getThreadKeepAliveTime() == -1 ? THREAD_KEEP_ALIVE_TIME
                : Configs.getThreadKeepAliveTime();
        TimeUnit timeUnit = Configs.getKeepAliveTimeUnit() == null ? KEEP_ALIVE_TIME_UNIT
                : Configs.getKeepAliveTimeUnit();
        int queueSize = Configs.getExecutorQueueSize() == -1 ? EXECUTOR_QUEUE_SIZE : Configs.getExecutorQueueSize();

        executors = new PriorityExecutor[coreSize];

        for (int i = 0; i < coreSize; i++) {
            executors[i] = new PriorityExecutor(keepAliveTime, timeUnit, queueSize);
            executors[i].init();
        }

        logger.info(
                "executor initialized successfully, core_size={}, thread_keep_alive_time={}, keep_alive_time_unit={}, executor_queue_size={}",
                coreSize, keepAliveTime, timeUnit, queueSize);
    }

    private void initLoadBalancer() {
        LoadBalanceType type = Configs.getLoadBalanceType() == null ? DEFAULT_LOAD_BALANCER
                : Configs.getLoadBalanceType();

        switch (type) {
        case WEIGHT_ROUND_ROBIN:
            loadBalancer = new WeightRoundRobinLoadBalancer();
            break;
        default:
            loadBalancer = new RoundRobinLoadBalancer();
            break;
        }

        logger.info("load balancer initialized successfully, type={}", type);
    }

    private Executor<Task> selectExecutor() {
        return loadBalancer.select(executors);
    }

}
