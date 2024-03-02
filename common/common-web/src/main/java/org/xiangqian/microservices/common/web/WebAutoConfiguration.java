package org.xiangqian.microservices.common.web;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验器
 * {@link org.springframework.web.servlet.DispatcherServlet#doDispatch(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)}
 * {@link org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter#handle(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse, java.lang.Object)}
 * {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#handleInternal(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod)}
 * {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#invokeHandlerMethod(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod)}
 * {@link org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod#invokeAndHandle(org.springframework.web.context.request.ServletWebRequest, org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)}
 * {@link org.springframework.web.method.support.InvocableHandlerMethod#getMethodArgumentValues(org.springframework.web.context.request.NativeWebRequest, org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)}
 * {@link org.springframework.web.method.annotation.ModelAttributeMethodProcessor#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)}
 * {@link org.springframework.web.method.annotation.ModelAttributeMethodProcessor#validateIfApplicable(org.springframework.web.bind.WebDataBinder, org.springframework.core.MethodParameter)}
 * {@link org.springframework.validation.DataBinder#validate(java.lang.Object...)}
 * {@link org.springframework.boot.autoconfigure.validation.ValidatorAdapter#validate(java.lang.Object, org.springframework.validation.Errors, java.lang.Object...)}
 * {@link org.springframework.validation.beanvalidation.SpringValidatorAdapter#validate(java.lang.Object, org.springframework.validation.Errors, java.lang.Object...)}
 * {@link org.springframework.validation.beanvalidation.SpringValidatorAdapter#processConstraintViolations(java.util.Set, org.springframework.validation.Errors)}
 * {@link org.hibernate.validator.internal.engine.ValidatorImpl#validate(java.lang.Object, java.lang.Class[])}
 * {@link org.hibernate.validator.internal.engine.ValidatorImpl#validateInContext(org.hibernate.validator.internal.engine.validationcontext.BaseBeanValidationContext, org.hibernate.validator.internal.engine.valuecontext.BeanValueContext, org.hibernate.validator.internal.engine.groups.ValidationOrder)}
 * {@link org.hibernate.validator.internal.engine.ValidatorImpl#validateConstraintsForCurrentGroup(org.hibernate.validator.internal.engine.validationcontext.BaseBeanValidationContext, org.hibernate.validator.internal.engine.valuecontext.BeanValueContext)}
 * {@link org.hibernate.validator.internal.engine.ValidatorImpl#validateConstraintsForNonDefaultGroup(org.hibernate.validator.internal.engine.validationcontext.BaseBeanValidationContext, org.hibernate.validator.internal.engine.valuecontext.BeanValueContext)}
 * {@link org.hibernate.validator.internal.engine.ValidatorImpl#validateMetaConstraints(org.hibernate.validator.internal.engine.validationcontext.BaseBeanValidationContext, org.hibernate.validator.internal.engine.valuecontext.ValueContext, java.lang.Object, java.lang.Iterable)}
 * {@link org.hibernate.validator.internal.engine.ValidatorImpl#validateMetaConstraint(org.hibernate.validator.internal.engine.validationcontext.BaseBeanValidationContext, org.hibernate.validator.internal.engine.valuecontext.ValueContext, java.lang.Object, org.hibernate.validator.internal.metadata.core.MetaConstraint)}
 * {@link org.hibernate.validator.internal.metadata.core.MetaConstraint#validateConstraint(org.hibernate.validator.internal.engine.validationcontext.ValidationContext, org.hibernate.validator.internal.engine.valuecontext.ValueContext)}
 * {@link org.hibernate.validator.internal.engine.constraintvalidation.SimpleConstraintTree#validateConstraints(org.hibernate.validator.internal.engine.validationcontext.ValidationContext, org.hibernate.validator.internal.engine.valuecontext.ValueContext, java.util.Collection)}
 * {@link org.hibernate.validator.internal.engine.constraintvalidation.ConstraintTree#validateSingleConstraint(org.hibernate.validator.internal.engine.valuecontext.ValueContext, org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl, jakarta.validation.ConstraintValidator)}
 * {@link jakarta.validation.ConstraintValidator#isValid(java.lang.Object, jakarta.validation.ConstraintValidatorContext)}
 * 综上所述，只要覆盖 {@link  org.hibernate.validator.internal.engine.constraintvalidation.SimpleConstraintTree} 即可处理增强校验注解。
 * 如何覆盖 {@link  org.hibernate.validator.internal.engine.constraintvalidation.SimpleConstraintTree} ？
 * {@link org.hibernate.validator.internal.engine.constraintvalidation.SimpleConstraintTree#SimpleConstraintTree(org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager, org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl, java.lang.reflect.Type)}
 * {@link org.hibernate.validator.internal.engine.constraintvalidation.ConstraintTree#of(org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager, org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl, java.lang.reflect.Type)}
 *
 * @author xiangqian
 * @date 20:48 2023/09/05
 */
@Slf4j
@Aspect
@Configuration(proxyBeanMethods = false)
public class WebAutoConfiguration {

    /**
     * controller层切面
     * <p>
     * {@link jakarta.validation.Valid} 先于 {@link org.aspectj.lang.annotation.Aspect} 执行
     * {@link org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)}
     * <p>
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(public * org.xiangqian.microservices..*.controller..*.*(..))" + // 'xxx..' 表示xxx包及其子包
            " && (@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    @Bean
    @Primary
    public WebExceptionHandler webExceptionHandler(List<WebExceptionHandler> handlers) {
        return new WebExceptionHandler() {
            @Override
            public int getOrder() {
                return HIGHEST_PRECEDENCE;
            }

            @Override
            public Response<?> handle(Throwable throwable, WebExceptionHandler.Chain chain) {
                handlers.get(0).handle(throwable, new WebExceptionHandler.Chain() {
                    private int index = 0;

                    @Override
                    public Response<?> next(Throwable throwable) {
                        index++;
                        if (index < handlers.size()) {
                            return handlers.get(index).handle(throwable, this);
                        }
                        return null;
                    }
                });
                return null;
            }
        };
    }

    // 绑定异常
    @Bean
    public WebExceptionHandler bindExceptionHandler() {
        return new WebExceptionHandler() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public Response<?> handle(Throwable throwable, Chain chain) {
                if (throwable instanceof BindException) {
                    BindException bindException = (BindException) throwable;
                    BindingResult bindingResult = bindException.getBindingResult();
                    String code = bindingResult.getFieldError().getDefaultMessage();
                    return Response.builder()
                            .code(code)
                            .build();
                }
                return chain.next(throwable);
            }
        };
    }

    // WebExchange绑定异常
    @Bean
    public WebExceptionHandler webExchangeBindExceptionHandler() {
        return new WebExceptionHandler() {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public Response<?> handle(Throwable throwable, Chain chain) {
                if (throwable instanceof WebExchangeBindException) {
                    WebExchangeBindException webExchangeBindException = (WebExchangeBindException) throwable;
                    BindingResult bindingResult = webExchangeBindException.getBindingResult();
                    String code = bindingResult.getFieldError().getDefaultMessage();
                    return Response.builder()
                            .code(code)
                            .build();
                }

                return chain.next(throwable);
            }
        };
    }

    // 服务输入异常
    @Bean
    public WebExceptionHandler serverWebInputExceptionHandler() {
        return new WebExceptionHandler() {
            @Override
            public int getOrder() {
                return 3;
            }

            @Override
            public Response<?> handle(Throwable throwable, Chain chain) {
                if (throwable instanceof ServerWebInputException) {
                    ServerWebInputException serverWebInputException = (ServerWebInputException) throwable;
                    return Response.builder()
                            .code(getCode(serverWebInputException.getStatusCode()))
                            .msg(serverWebInputException.getReason())
                            .build();
                }
                return chain.next(throwable);
            }
        };
    }

    // 响应状态异常
    @Bean
    public WebExceptionHandler responseStatusExceptionHandler() {
        return new WebExceptionHandler() {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public Response<?> handle(Throwable throwable, Chain chain) {
                if (throwable instanceof ResponseStatusException) {
                    ResponseStatusException responseStatusException = (ResponseStatusException) throwable;
                    return Response.builder()
                            .code(getCode(responseStatusException.getStatusCode()))
                            .msg(responseStatusException.getReason())
                            .build();
                }
                return chain.next(throwable);
            }
        };
    }

    // 错误响应异常
    @Bean
    public WebExceptionHandler errorResponseExceptionHandler() {
        return new WebExceptionHandler() {
            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public Response<?> handle(Throwable throwable, Chain chain) {
                if (throwable instanceof ErrorResponseException) {
                    ErrorResponseException errorResponseException = (ErrorResponseException) throwable;
                    return Response.builder()
                            .code(getCode(errorResponseException.getStatusCode()))
                            .msg(errorResponseException.getMessage())
                            .build();
                }
                return chain.next(throwable);
            }
        };
    }

    // 断言异常
    @Bean
    public WebExceptionHandler assertExceptionHandler() {
        return new WebExceptionHandler() {
            @Override
            public int getOrder() {
                return 6;
            }

            @Override
            public Response<?> handle(Throwable throwable, Chain chain) {
                if (throwable instanceof Assert.Exception) {
                    Assert.Exception assertException = (Assert.Exception) throwable;
                    return Response.builder()
                            .code(assertException.getMessage())
                            .build();
                }
                return chain.next(throwable);
            }
        };
    }

    // 重复主键异常
    @Bean
    public WebExceptionHandler duplicateKeyExceptionHandler() {
        return new WebExceptionHandler() {
            // 引号 '' 正则表达式
            private final Pattern QUOTE_PATTERN = Pattern.compile("'([^']*)'");

            @Override
            public int getOrder() {
                return 7;
            }

            @Override
            public Response<?> handle(Throwable throwable, Chain chain) {
                if (throwable instanceof DuplicateKeyException) {
                    DuplicateKeyException duplicateKeyException = (DuplicateKeyException) throwable;
                    Throwable cause = duplicateKeyException.getCause();

                    // SQL完整性约束违反异常
                    if (cause instanceof SQLIntegrityConstraintViolationException) {
                        // Duplicate entry '18700000001' for key 'phone';
                        Matcher matcher = QUOTE_PATTERN.matcher(cause.getMessage());
                        String entry = null;
                        String key = null;
                        if (matcher.find()) {
                            entry = matcher.group(0);
                            entry = entry.substring(1, entry.length() - 1);
                        }
                        if (matcher.find()) {
                            key = matcher.group(0);
                            key = key.substring(1, key.length() - 1);
                            if ("phone".equals(key)) {
                                key = "手机号";
                            } else if ("username".equals(key)) {
                                key = "登录名";
                            }
                        }

                        return Response.builder()
                                .code(WebCode.INTERNAL_SERVER_ERROR)
                                .msg(String.format("%s%s已存在", key, entry))
                                .build();
                    }

                    return Response.builder()
                            .code(duplicateKeyException.getMessage())
                            .build();
                }
                return chain.next(throwable);
            }
        };
    }

    // 默认异常
    @Bean
    public WebExceptionHandler defaultWebExceptionHandler() {
        return new WebExceptionHandler() {
            @Override
            public Response<?> handle(Throwable throwable, Chain chain) {
                return Response.builder()
                        .code(WebCode.INTERNAL_SERVER_ERROR)
                        .msg(throwable.getMessage())
                        .build();
            }

            @Override
            public int getOrder() {
                return LOWEST_PRECEDENCE;
            }
        };
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
