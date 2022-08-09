package org.microservices.common.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * @author xiangqian
 * @date 11:02 2020/11/09
 */
public class JacksonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // ================ serialize ================

    public static String toJson(Object object) throws IOException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static String toFormatJson(Object object) throws IOException {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static byte[] toBytes(Object object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    // ================ deserialize ================

    public static <T> T toObject(String json, Class<T> type) throws IOException {
        return OBJECT_MAPPER.readValue(json, type);
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) throws IOException {
        return OBJECT_MAPPER.readValue(json, typeReference);
    }

    public static <T> T toObject(byte[] bytes, Class<T> type) throws IOException {
        return OBJECT_MAPPER.readValue(bytes, type);
    }

    public static JsonNode toJsonNode(String json) throws IOException {
        return OBJECT_MAPPER.readTree(json);
    }

    public static <T> T toObject(JsonNode jsonNode, Class<T> type) throws IOException {
        return toObject(toJson(jsonNode), type);
    }

    public static <T> T toObject(JsonNode jsonNode, TypeReference<T> typeReference) throws IOException {
        return toObject(toJson(jsonNode), typeReference);
    }

    // ================ create node ================

    public static ObjectNode createObjectNode() {
        return OBJECT_MAPPER.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return OBJECT_MAPPER.createArrayNode();
    }

}
