package org.microservices.common.register.support;

import lombok.Data;

/**
 * consul配置文件
 *
 * @author xiangqian
 * @date 20:13:37 2022/04/30
 */
@Data
public class Config {

    private String name;
    private String content;

}
