package org.xiangqian.microservices.common.register;

import java.util.List;

/**
 * 配置文件服务
 *
 * @author xiangqian
 * @date 20:28 2023/10/10
 */
public interface ConfigService {

    /**
     * 获取配置文件集
     *
     * @return
     */
    List<Config> gets();

}
