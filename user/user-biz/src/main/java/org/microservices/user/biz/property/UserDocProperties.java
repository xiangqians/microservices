package org.microservices.user.biz.property;

import org.microservices.common.doc.support.DefaultDocProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiangqian
 * @date 11:19 2022/04/10
 */
@Component
public class UserDocProperties extends DefaultDocProperties {

    @Override
    public String description() {
        return "User API文档接口";
    }

}
