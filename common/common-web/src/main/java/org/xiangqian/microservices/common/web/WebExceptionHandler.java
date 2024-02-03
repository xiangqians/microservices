package org.xiangqian.microservices.common.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.xiangqian.microservices.common.model.Response;
import org.xiangqian.microservices.common.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author xiangqian
 * @date 20:38 2023/10/16
 */
@Slf4j
public abstract class WebExceptionHandler {

    protected Response<?> handle(Throwable throwable) {
        log.error("", throwable);
        if (throwable instanceof BindException) {
            return handle((BindException) throwable);
        }

        if (throwable instanceof WebExchangeBindException) {
            return handle((WebExchangeBindException) throwable);
        }

        if (throwable instanceof ServerWebInputException) {
            return handle((ServerWebInputException) throwable);
        }

        if (throwable instanceof ResponseStatusException) {
            return handle((ResponseStatusException) throwable);
        }

        if (throwable instanceof ErrorResponseException) {
            return handle((ErrorResponseException) throwable);
        }

        if (throwable instanceof Assert.Exception) {
            return handle((Assert.Exception) throwable);
        }

        return Response.builder()
                .code(WebCode.INTERNAL_SERVER_ERROR)
                .msg(throwable.getMessage())
                .build();
    }

    // 引号 '' 正则表达式
    private final Pattern QUOTE_PATTERN = Pattern.compile("'([^']*)'");

//    private Response<?> handleDuplicateKeyException(DuplicateKeyException duplicateKeyException) {
//        Throwable cause = duplicateKeyException.getCause();
//
//        // SQL完整性约束违反异常
//        if (cause instanceof SQLIntegrityConstraintViolationException) {
//            // Duplicate entry '18700000001' for key 'phone';
//            Matcher matcher = QUOTE_PATTERN.matcher(cause.getMessage());
//            String entry = null;
//            String key = null;
//            if (matcher.find()) {
//                entry = matcher.group(0);
//                entry = entry.substring(1, entry.length() - 1);
//            }
//            if (matcher.find()) {
//                key = matcher.group(0);
//                key = key.substring(1, key.length() - 1);
//                if ("phone".equals(key)) {
//                    key = "手机号";
//                } else if ("username".equals(key)) {
//                    key = "登录名";
//                }
//            }
//
//            return Response.builder()
//                    .code(internalServerErrorCode)
//                    .msg(String.format("%s%s已存在", key, entry))
//                    .build();
//        }
//
//        return handleDefaultException(duplicateKeyException);
//    }

    // 处理断言异常
    protected Response<?> handle(Assert.Exception exception) {
        String code = exception.getMessage();
        return Response.builder()
                .code(code)
                .build();
    }

    // 处理响应状态异常
    protected Response<?> handle(ResponseStatusException exception) {
        return Response.builder()
                .code(getCode(exception.getStatusCode()))
                .msg(exception.getReason())
                .build();
    }

    // 处理服务输入异常
    protected Response<?> handle(ServerWebInputException exception) {
        return Response.builder()
                .code(getCode(exception.getStatusCode()))
                .msg(exception.getReason())
                .build();
    }

    // 处理错误响应异常
    protected Response<?> handle(ErrorResponseException exception) {
        return Response.builder()
                .code(getCode(exception.getStatusCode()))
                .msg(exception.getMessage())
                .build();
    }

    // 处理WebExchange绑定异常
    protected Response<?> handle(WebExchangeBindException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return handle(bindingResult);
    }

    // 处理绑定异常
    protected Response<?> handle(BindException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return handle(bindingResult);
    }

    // 处理绑定结果异常
    protected Response<?> handle(BindingResult bindingResult) {
        String code = bindingResult.getFieldError().getDefaultMessage();
        return Response.builder()
                .code(code)
                .build();
    }

    private String getCode(HttpStatusCode httpStatusCode) {
        if (Objects.isNull(httpStatusCode)) {
            return WebCode.INTERNAL_SERVER_ERROR;
        }

        if (httpStatusCode.is1xxInformational()) {
            return WebCode.CONTINUE;
        }

        if (httpStatusCode.is2xxSuccessful()) {
            return WebCode.OK;
        }

        if (httpStatusCode.is3xxRedirection()) {
            return WebCode.MULTIPLE_CHOICES;
        }

        if (httpStatusCode.is4xxClientError()) {
            if (httpStatusCode instanceof HttpStatus) {
                switch ((HttpStatus) httpStatusCode) {
                    case BAD_REQUEST:
                        return WebCode.BAD_REQUEST;
                    case UNAUTHORIZED:
                        return WebCode.UNAUTHORIZED;
                    case PAYMENT_REQUIRED:
                        return WebCode.PAYMENT_REQUIRED;
                    case FORBIDDEN:
                        return WebCode.FORBIDDEN;
                    case NOT_FOUND:
                        return WebCode.NOT_FOUND;
                    case METHOD_NOT_ALLOWED:
                        return WebCode.METHOD_NOT_ALLOWED;
                    case NOT_ACCEPTABLE:
                        return WebCode.NOT_ACCEPTABLE;
                    case PROXY_AUTHENTICATION_REQUIRED:
                        return WebCode.PROXY_AUTHENTICATION_REQUIRED;
                    case REQUEST_TIMEOUT:
                        return WebCode.REQUEST_TIMEOUT;
                    case CONFLICT:
                        return WebCode.CONFLICT;
                    case GONE:
                        return WebCode.GONE;
                    case LENGTH_REQUIRED:
                        return WebCode.LENGTH_REQUIRED;
                    case PRECONDITION_FAILED:
                        return WebCode.PRECONDITION_FAILED;
                    case PAYLOAD_TOO_LARGE:
                        return WebCode.PAYLOAD_TOO_LARGE;
                    case REQUEST_ENTITY_TOO_LARGE:
                        return WebCode.REQUEST_ENTITY_TOO_LARGE;
                    case URI_TOO_LONG:
                        return WebCode.URI_TOO_LONG;
                    case REQUEST_URI_TOO_LONG:
                        return WebCode.REQUEST_URI_TOO_LONG;
                    case UNSUPPORTED_MEDIA_TYPE:
                        return WebCode.UNSUPPORTED_MEDIA_TYPE;
                    case REQUESTED_RANGE_NOT_SATISFIABLE:
                        return WebCode.REQUESTED_RANGE_NOT_SATISFIABLE;
                    case EXPECTATION_FAILED:
                        return WebCode.EXPECTATION_FAILED;
                    case I_AM_A_TEAPOT:
                        return WebCode.I_AM_A_TEAPOT;
                    case INSUFFICIENT_SPACE_ON_RESOURCE:
                        return WebCode.INSUFFICIENT_SPACE_ON_RESOURCE;
                    case METHOD_FAILURE:
                        return WebCode.METHOD_FAILURE;
                    case DESTINATION_LOCKED:
                        return WebCode.DESTINATION_LOCKED;
                    case UNPROCESSABLE_ENTITY:
                        return WebCode.UNPROCESSABLE_ENTITY;
                    case LOCKED:
                        return WebCode.LOCKED;
                    case FAILED_DEPENDENCY:
                        return WebCode.FAILED_DEPENDENCY;
                    case TOO_EARLY:
                        return WebCode.TOO_EARLY;
                    case UPGRADE_REQUIRED:
                        return WebCode.UPGRADE_REQUIRED;
                    case PRECONDITION_REQUIRED:
                        return WebCode.PRECONDITION_REQUIRED;
                    case TOO_MANY_REQUESTS:
                        return WebCode.TOO_MANY_REQUESTS;
                    case REQUEST_HEADER_FIELDS_TOO_LARGE:
                        return WebCode.REQUEST_HEADER_FIELDS_TOO_LARGE;
                    case UNAVAILABLE_FOR_LEGAL_REASONS:
                        return WebCode.UNAVAILABLE_FOR_LEGAL_REASONS;
                }
            }
            return WebCode.BAD_REQUEST;
        }

        if (httpStatusCode.is5xxServerError()) {
            return WebCode.INTERNAL_SERVER_ERROR;
        }

        return WebCode.INTERNAL_SERVER_ERROR;
    }

}
