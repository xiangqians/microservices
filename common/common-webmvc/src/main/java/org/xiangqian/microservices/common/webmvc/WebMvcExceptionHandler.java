package org.xiangqian.microservices.common.webmvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xiangqian.microservices.common.model.Response;
import org.xiangqian.microservices.common.web.WebExceptionHandler;

/**
 * @author xiangqian
 * @date 20:43 2023/10/16
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RestControllerAdvice(basePackages = {"org.xiangqian.microservices"})
public class WebMvcExceptionHandler extends WebExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(Exception exception) {
        return handle(exception);
    }

}
