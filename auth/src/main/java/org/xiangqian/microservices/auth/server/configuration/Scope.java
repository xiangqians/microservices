package org.xiangqian.microservices.auth.server.configuration;

/**
 * 自定义scope
 *
 * @author xiangqian
 * @date 19:14 2023/07/21
 */
public interface Scope {
    String ALL = "all";
    String READ = "read";
    String WRITE = "write";
}
