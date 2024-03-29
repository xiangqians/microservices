package org.xiangqian.microservices.user.model;

import org.xiangqian.microservices.common.model.Code;

/**
 * @author xiangqian
 * @date 21:19 2023/09/05
 */
public interface UserCode extends Code {

    @Description(zh = "手机号不能为空")
    String PHONE_NOT_EMPTY = "phone_not_empty";

}
