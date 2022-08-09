package org.microservices.common.doc.support;

import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.MethodParameter;

import java.util.Objects;

/**
 * customizers see {@link org.springdoc.core.customizers}
 *
 * @author xiangqian
 * @date 21:05 2022/06/22
 */
public class ParameterCustomizerImpl implements ParameterCustomizer {

    @Override
    public Parameter customize(Parameter parameterModel, MethodParameter methodParameter) {
        // HTTP POST, parameterModel == null ?
        if (Objects.isNull(parameterModel)) {
            return null;
        }

//        Class<?> returnType = methodParameter.getMethod().getReturnType();
//        // org.microservices.common.core.enumeration.Enum
//        if (org.microservices.common.core.enumeration.Enum.class.isAssignableFrom(returnType)
//                // java.lang.Enum
//                && java.lang.Enum.class.isAssignableFrom(returnType)) {
//            Optional.ofNullable(DocHelper.createEnumSchema((Class<org.microservices.common.core.enumeration.Enum>) returnType)).ifPresent(parameterModel::setSchema);
//        }

        // GET请求参数不显示 {@link io.swagger.v3.oas.annotations.media.Schema.description} 信息
        if (Objects.isNull(parameterModel.getDescription())) {
            parameterModel.setDescription(parameterModel.getSchema().getDescription());
        }

        return parameterModel;
    }

}
