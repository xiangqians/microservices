package org.xiangqian.microservices.common.register;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

/**
 * 配置文件服务
 *
 * @author xiangqian
 * @date 20:28 2023/10/10
 */
public interface ConfigService {

    /**
     * 获取配置文件数据集
     *
     * @return
     */
    List<Config> gets();

    static ConfigService get() throws IOException, ReflectiveOperationException {
        String className = IOUtils.resourceToString("META-INF/org.xiangqian.microservices.common.register.ConfigService.import",
                StandardCharsets.UTF_8,
                Thread.currentThread().getContextClassLoader());
        Class<Supplier<List<Config>>> c = (Class<Supplier<List<Config>>>) Class.forName(className);
        return (ConfigService) c.getConstructor().newInstance();
    }

}
