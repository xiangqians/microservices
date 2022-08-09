package org.microservices.common.register.support;

/**
 * 配置文件监听
 *
 * @author xiangqian
 * @date 18:57:31 2022/04/30
 */
public interface ConfigListener {

    /**
     * init
     *
     * @param config
     */
    void init(Config config);

    /**
     * changed
     *
     * @param config
     * @return 是否交由consul继续处理
     */
    boolean changed(Config config);

}
