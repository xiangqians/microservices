package org.xiangqian.microservices.common.web;

import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.xiangqian.microservices.common.model.Vo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author xiangqian
 * @date 20:48 2023/09/05
 */
@Slf4j
@Aspect
@Configuration(proxyBeanMethods = false)
public class WebAutoConfiguration {

    @Bean
    public Validator validator(LocalValidatorFactoryBean localValidatorFactoryBean) {
        return new Validator(localValidatorFactoryBean);
    }

    // 'xxx..' 表示xxx包及其子包
    @Pointcut("execution(public * org.xiangqian.microservices..*.controller..*.*(..)) && (@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void pointcut() {
    }

    /**
     * 环绕通知
     * <p>
     * {@link jakarta.validation.Valid} 先于 {@link org.aspectj.lang.annotation.Aspect} 执行
     * {@link org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)}
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        TaskExecutor executor = SpringUtil.getBean(TaskExecutor.class);
//        System.err.println("executor--------" + executor);
        // No qualifying bean of type 'org.springframework.core.task.TaskExecutor' available:
        // expected single matching bean but found 3: applicationTaskExecutor,configWatchTaskScheduler,catalogWatchTaskScheduler

        // 校验vo参数
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args)) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            Annotation[][] paramsAnnotations = method.getParameterAnnotations();
            int paramsAnnotationsLen = paramsAnnotations.length;
            for (int i = 0, len = args.length; i < len; i++) {
                Object arg = args[i];
                if (Objects.isNull(arg) || !(arg instanceof Vo) || i >= paramsAnnotationsLen) {
                    continue;
                }

                Annotation[] paramAnnotations = paramsAnnotations[i];
                if (ArrayUtils.isNotEmpty(paramAnnotations)) {
                    Vo vo = (Vo) arg;
                    for (Annotation paramAnnotation : paramAnnotations) {
                        if (Objects.nonNull(paramAnnotation) && paramAnnotation instanceof Validated) {
                            Validated validated = (Validated) paramAnnotation;
                            Class<?>[] groups = validated.value();
                            if (ArrayUtils.isNotEmpty(groups)) {
                                vo.validate(groups);
                            } else {
                                vo.validate(Default.class);
                            }
                        }
                    }
                }
            }
        }

        return joinPoint.proceed();
    }

}
