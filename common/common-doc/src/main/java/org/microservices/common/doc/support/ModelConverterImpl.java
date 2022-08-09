package org.microservices.common.doc.support;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.microservices.common.core.enumeration.Enum;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Objects;

/**
 * customizers see {@link org.springdoc.core.customizers}
 *
 * @author xiangqian
 * @date 21:03 2022/06/22
 */
public class ModelConverterImpl implements ModelConverter {

    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext modelConverterContext, Iterator<ModelConverter> iterator) {
        // resolve
        Schema schema = iterator.hasNext() ? iterator.next().resolve(annotatedType, modelConverterContext, iterator) : null;

        // resolve Enum
        schema = resolveEnum(schema, annotatedType);

        return schema;
    }

    private Schema resolveEnum(Schema schema, AnnotatedType annotatedType) {
        Type type = annotatedType.getType();

        Class<?> c = null;
        // Class
        // GET请求模型
        if (type instanceof Class<?>) {
            c = (Class<?>) type;

        }
        // JavaType
        // POST请求模型 & 响应模型
        else if (type instanceof JavaType) {
            JavaType javaType = (JavaType) type;
            c = javaType.getRawClass();

        } else {
            return schema;
        }

        // org.microservices.common.core.enumeration.Enum
        if (Enum.class.isAssignableFrom(c)
                // java.lang.Enum
                && java.lang.Enum.class.isAssignableFrom(c)) {
            Schema enumSchema = DocHelper.createEnumSchema((Class<Enum>) c);
            enumSchema.setDescription(String.format("%s, %s", schema.getDescription(), enumSchema.getDescription()));
            return enumSchema;
        }

        return schema;
    }


}
