package org.coral.jroutine.schedule.lb;

import org.coral.jroutine.Task;
import org.coral.jroutine.schedule.Executor;

/**
 * round robin for load balance.
 * 
 * @author lihao
 * @date 2020-05-12
 */
public class WeightRoundRobinLoadBalancer implements LoadBalancer<Executor<Task>> {

    @Override
    public Executor<Task> select(Executor<Task>[] executors) {
        return null;
    }

}
