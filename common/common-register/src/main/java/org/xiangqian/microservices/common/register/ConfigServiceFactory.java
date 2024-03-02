package org.xiangqian.microservices.common.register;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author xiangqian
 * @date 13:02 2024/01/29
 */
public class ConfigServiceFactory {

    private static volatile ConfigService configService;

    public static ConfigService get() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (configService != null) {
            return configService;
        }

        synchronized (ConfigServiceFactory.class) {
            if (configService == null) {
                configService = new ConfigServiceImpl();
            }
        }
        return configService;
    }

}
