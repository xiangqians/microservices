package org.xiangqian.microservices.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

/**
 * @author xiangqian
 * @date 21:02 2020/11/09
 */
public class JsonUtil {

    private static final ObjectMapper om = new ObjectMapper();

    static {
        // 序列化包含设置
//        om.setSerializationInclusion(JsonInclude.Include.ALWAYS); // 总是格式化（默认）
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 属性为NULL不序列化

        // 查找和注册模块
//        om.findAndRegisterModules();

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 本地日期序列化/反序列化器
        DateTimeFormatter formatter = DateUtil.getFormatter("yyyy/MM/dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));

        // 本地时间序列化/反序列化器
        formatter = DateUtil.getFormatter("HH:mm:ss");
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(formatter));

        // 本地日期时间序列化/反序列化器
        formatter = DateUtil.getFormatter("yyyy/MM/dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));

        om.registerModule(javaTimeModule);
    }

    public static String serializeAsPrettyString(Object object) throws IOException {
        return om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static String serializeAsString(Object object) throws IOException {
        return om.writeValueAsString(object);
    }

    public static byte[] serializeAsBytes(Object object) throws IOException {
        return om.writeValueAsBytes(object);
    }

    public static <T> T deserialize(String text, Class<T> type) throws IOException {
        return om.readValue(text, type);
    }

    public static <T> T deserialize(String text, TypeReference<T> typeRef) throws IOException {
        return om.readValue(text, typeRef);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        return om.readValue(bytes, type);
    }

    public static <T> T deserialize(byte[] bytes, TypeReference<T> typeRef) throws IOException {
        return om.readValue(bytes, typeRef);
    }

    public static <T> T deserialize(Map map, Class<T> type) {
        return om.convertValue(map, type);
    }

    public static <T> T deserialize(Map map, TypeReference<T> typeRef) {
        return om.convertValue(map, typeRef);
    }

    public static <T> T deserialize(Collection collection, Class<T> type) {
        return om.convertValue(collection, type);
    }

    public static <T> T deserialize(Collection collection, TypeReference<T> typeRef) {
        return om.convertValue(collection, typeRef);
    }

    public static JsonNode readTree(String text) throws IOException {
        return om.readTree(text);
    }

    public static JsonNode readTree(byte[] bytes) throws IOException {
        return om.readTree(bytes);
    }

    public static ObjectNode createObjectNode() {
        return om.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return om.createArrayNode();
    }

}
