package org.xiangqian.microservices.common.web;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 校验器
 * <p>
 * 增强校验器？
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
 * @date 20:42 2022/09/22
 */
public class Validator {

    private LocalValidatorFactoryBean localValidatorFactoryBean;

    Validator(LocalValidatorFactoryBean localValidatorFactoryBean) {
        this.localValidatorFactoryBean = localValidatorFactoryBean;
    }

    /**
     * 校验
     *
     * @param object 校验对象
     * @param groups 校验组
     * @param <T>
     * @return 校验异常集合
     */
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        if (ArrayUtils.isNotEmpty(groups)) {
            return localValidatorFactoryBean.validate(object, groups);
        }
        return localValidatorFactoryBean.validate(object);
    }

    /**
     * 校验，如果有异常则抛出
     *
     * @param object
     * @param groups
     * @param <T>
     */
    public <T> void validateIfException(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> validationResults = validate(object, groups);
        if (CollectionUtils.isNotEmpty(validationResults)) {
            throw new ValidationException(validationResults.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("、")));
        }
    }

}
