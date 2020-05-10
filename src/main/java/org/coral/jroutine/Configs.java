package org.coral.jroutine;

/**
 * Configs
 * 
 * @author lihao
 * @date 2020-05-06
 */
public class Configs {

    private static boolean debug = true;
    private static ExtensionType extensionType = ExtensionType.METHOD;

    public static boolean isDebugEnabled() {
        return debug;
    }

    public static ExtensionType getExtensionType() {
        return extensionType;
    }
}
