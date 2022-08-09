package org.microservices.common.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.microservices.common.core.resp.*;
import org.microservices.common.web.exception.ServerException;

/**
 * 全局异常处理器
 *
 * @author xiangqian
 * @date 10:54 2022/03/12
 */
@Slf4j
@Aspect
public class GlobalExceptionHandler {

    @Pointcut("execution(public * org.microservices..*.controller.*.*(..)) && (@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.debug(">>>>> 执行目标方法: {}.{}(..)", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private Response<?> handleException(Exception exception) {
        Version version = null;
        StatusCode statusCode = null;
        String message = null;
        if (exception instanceof ServerException) {
            ServerException serverException = (ServerException) exception;
            version = serverException.getVersion();
            statusCode = serverException.getStatusCode();
            message = serverException.getMessage();

        } else {
            version = VersionImpl.V202203;
            statusCode = StatusCodeImpl.INTERNAL_SERVER_ERROR;
            message = exception.getLocalizedMessage();
        }

        log.error(String.format("%s, %s, %s", version, statusCode, message), exception);
        return Response.builder()
                .version(version)
                .statusCode(statusCode)
                .message(message)
                .build();
    }

}
