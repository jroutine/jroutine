package org.coral.jroutine.schedule.lb;

/**
 * unit of load balancer.
 * 
 * @author lihao
 * @date 2020-05-12
 */
public interface Instance {

    default int getWeight() {
        return -1;
    }
}
