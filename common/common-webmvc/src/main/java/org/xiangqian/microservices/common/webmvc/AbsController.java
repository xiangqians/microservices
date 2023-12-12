package org.xiangqian.microservices.common.webmvc;

import org.xiangqian.microservices.common.model.Response;
import org.xiangqian.microservices.common.util.MessageUtil;
import org.xiangqian.microservices.common.web.WebCode;

/**
 * @author xiangqian
 * @date 20:57 2023/10/16
 */
public abstract class AbsController {

    protected <T> Response<T> response(T t) {
        String code = WebCode.OK;
        return Response.<T>builder()
                .code(code)
                .msg(MessageUtil.get(code))
                .data(t)
                .build();
    }

}
