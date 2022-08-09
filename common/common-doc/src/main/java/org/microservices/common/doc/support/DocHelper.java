package org.microservices.common.doc.support;

import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.microservices.common.core.enumeration.Enum;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangqian
 * @date 21:15 2022/06/22
 */
@Slf4j
public class DocHelper {

    public static Schema createEnumSchema(Class<Enum> type) {
        Enum[] enumSchemas = null;
        try {
            enumSchemas = (Enum[]) type.getMethod("values").invoke(null);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("", e);
            return null;
        }

        if (ArrayUtils.isEmpty(enumSchemas)) {
            return null;
        }

        Schema schema = null;
        Class<?> tClass = enumSchemas[0].getValue().getClass();
        if (tClass == Integer.class) {
            schema = new IntegerSchema();
        } else if (tClass == String.class) {
            schema = new StringSchema();
        } else {
            return null;
        }

        int length = enumSchemas.length;
        List<Object> values = new ArrayList<>(length);
        StringBuilder descriptionBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            Enum enumSchema = enumSchemas[i];

            // value
            values.add(enumSchema.getValue());

            // description
            if (i > 0) {
                descriptionBuilder.append("; ");
            }
            descriptionBuilder.append(enumSchema.getValue()).append(", ").append(enumSchema.getDescription());
        }

        schema.setEnum(values);
        schema.setDescription(descriptionBuilder.toString());
        return schema;
    }

}
