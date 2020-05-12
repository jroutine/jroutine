package org.coral.jroutine.config;

/**
 * Configs
 * 
 * @author lihao
 * @date 2020-05-06
 */
public class Configs {

    private static boolean debug = true;
    private static int executorsCoreSize = 0;
    private static ExtensionType extensionType = ExtensionType.METHOD;
    private static LoadBalanceType loadBalanceType = LoadBalanceType.ROUND_ROBIN;

    public static boolean isDebugEnabled() {
        return debug;
    }

    public static int getExecutorsCoreSize() {
        return executorsCoreSize;
    }

    public static ExtensionType getExtensionType() {
        return extensionType;
    }

    public static LoadBalanceType getLoadBalanceType() {
        return loadBalanceType;
    }

}
