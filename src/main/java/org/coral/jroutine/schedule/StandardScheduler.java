package org.coral.jroutine.schedule;

import org.coral.jroutine.AbstractLifecycle;
import org.coral.jroutine.Task;
import org.coral.jroutine.config.Configs;
import org.coral.jroutine.exception.LifecycleException;
import org.coral.jroutine.schedule.lb.LoadBalancer;
import org.coral.jroutine.schedule.lb.RoundRobinLoadBalancer;
import org.coral.jroutine.schedule.lb.WeightRoundRobinLoadBalancer;

/**
 * the standard scheduler, assigns executor to the submitted task.
 * 
 * @author lihao
 * @date 2020-05-12
 */
public class StandardScheduler extends AbstractLifecycle implements Scheduler<Task> {

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
    }

    @Override
    protected void stopInternal() throws LifecycleException {
        for (Executor<Task> executor : executors) {
            executor.stop();
        }
    }

    @Override
    public void submit(Task task) {
        Executor<Task> executor = selectExecutor();
        executor.execute(task);
    }

    private void initExecutors() {
        int coreSize = Configs.getExecutorsCoreSize() == 0 ? Runtime.getRuntime().availableProcessors()
                : Configs.getExecutorsCoreSize();
        executors = new PriorityExecutor[coreSize];
        for (int i = 0; i < coreSize; i++) {
            executors[i] = new PriorityExecutor();
            executors[i].init();
        }
    }

    private void initLoadBalancer() {
        switch (Configs.getLoadBalanceType()) {
        case WEIGHT_ROUND_ROBIN:
            loadBalancer = new WeightRoundRobinLoadBalancer();
            break;
        default:
            loadBalancer = new RoundRobinLoadBalancer();
            break;
        }
    }

    private Executor<Task> selectExecutor() {
        return loadBalancer.select(executors);
    }

}
