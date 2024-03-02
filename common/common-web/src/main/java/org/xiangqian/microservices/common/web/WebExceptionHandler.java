package org.xiangqian.microservices.common.web;

import org.springframework.core.Ordered;
import org.xiangqian.microservices.common.model.Response;

/**
 * @author xiangqian
 * @date 20:38 2024/02/29
 */
public interface WebExceptionHandler extends Ordered {

    Response<?> handle(Throwable throwable, Chain chain);

    interface Chain {
        Response<?> next(Throwable throwable);
    }

}
