package org.microservices.common.core.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author xiangqian
 * @date 16:49:22 2022/03/19
 */
public class JVMUtils {

    public static int pid() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String name = runtimeMXBean.getName();
        return Integer.parseInt(name.substring(0, name.indexOf("@")));
    }

}
