package org.xiangqian.microservices.common.register;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

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
                String className = IOUtils.resourceToString("META-INF/org.xiangqian.microservices.common.register.ConfigService.import",
                        StandardCharsets.UTF_8,
                        Thread.currentThread().getContextClassLoader());
                Class<Supplier<List<Config>>> c = (Class<Supplier<List<Config>>>) Class.forName(className);
                configService = (ConfigService) c.getConstructor().newInstance();
            }
        }
        return configService;
    }

}
