package org.xiangqian.microservices.common.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置文件
 *
 * @author xiangqian
 * @date 20:47 2023/09/04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {

    // 名称
    private String name;

    // 内容
    private String content;

}
