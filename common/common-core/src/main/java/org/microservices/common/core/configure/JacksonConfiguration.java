package org.microservices.common.core.configure;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.microservices.common.core.enumeration.Enum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Jackson配置
 * <p>
 * 源码分析：
 * 创建ObjectMapper：
 * {@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration}
 * {@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperConfiguration#jacksonObjectMapper(org.springframework.http.converter.json.Jackson2ObjectMapperBuilder)}
 * <p>
 * 初始化Jackson2ObjectMapperBuilder
 * {@link  org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration#jacksonObjectMapperBuilder(org.springframework.context.ApplicationContext, java.util.List)}
 * {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder} prototype原型模式，指的是每次调用时，会重新创建该类的一个实例，比较类似于我们自己自己new的对象实例。默认的singleton单实例模式
 * 根据源码分析，只需要将 {@link org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer} 实例注入IOC容器，即可实现自定义配置
 *
 * @author xiangqian
 * @date 11:29 2022/06/19
 */
@Configuration
public class JacksonConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Value("${spring.jackson.time-zone:GMT+8}")
    private String timeZone;

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        customizeDate(jacksonObjectMapperBuilder);
        customizeEnum(jacksonObjectMapperBuilder);
    }

    private void customizeEnum(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        // 反序列化
        jacksonObjectMapperBuilder.deserializerByType(Enum.class, new JsonDeserializer<Enum>() {
            @Override
            public Enum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
                return null;
            }
        });

        // 序列化
        jacksonObjectMapperBuilder.serializerByType(java.lang.Enum.class, new JsonSerializer<Enum>() {
            @Override
            public void serialize(Enum iEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("value1", String.valueOf(iEnum.getValue()));
                jsonGenerator.writeStringField("description", iEnum.getDescription());
                jsonGenerator.writeEndObject();
            }
        });
    }

    private void customizeDate(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        // Date
        DateFormat df = new SimpleDateFormat(dateFormat);
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        jacksonObjectMapperBuilder.dateFormat(df);

        // LocalDateTime
        // 序列号LocalDateTime
        jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormat)));
        // 反序列化LocalDateTime
        jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormat)));

        // TimeZone
        jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone(timeZone));
    }

}
