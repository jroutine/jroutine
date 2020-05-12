package org.coral.jroutine.schedule.lb;

/**
 * load balancer.
 * 
 * @author lihao
 * @date 2020-05-12
 */
public interface LoadBalancer<I extends Instance> {

    public I select(I[] executors);
}
