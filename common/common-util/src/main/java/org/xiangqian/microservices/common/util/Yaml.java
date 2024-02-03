package org.xiangqian.microservices.common.util;

import org.apache.commons.collections4.MapUtils;

import java.io.InputStream;
import java.util.*;

/**
 * @author xiangqian
 * @date 20:32 2023/05/19
 */
public class Yaml {

    private Map<String, String> map;

    public Yaml(String content) {
        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
        this.map = yaml.loadAs(content, Map.class);
    }

    public Yaml(InputStream inputStream) {
        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
        this.map = yaml.loadAs(inputStream, Map.class);
    }

    public Integer getInt(String key) {
        String value = getString(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public String getString(String key) {
        Object objValue = get(key);
        if (objValue != null) {
            String value = objValue.toString().trim();

            // eg: ${REGISTER_HOST:register}
            if (value.startsWith("${") && value.contains(":") && value.endsWith("}")) {
                int index = value.indexOf(":");
                String name = value.substring("${".length(), index);
                return Optional.ofNullable(System.getenv(name)).orElse(value.substring(index + 1, value.length() - "}".length()));
            }

            return value;
        }

        return null;
    }

    public List getList(String key) {
        Object value = get(key);
        if (value instanceof List) {
            return (List<String>) value;
        }
        return null;
    }

    private Object get(String key) {
        Queue<String> keyQueue = new LinkedList<>();
        Arrays.stream(key.split("\\.")).forEach(keyQueue::add);
        return get(map, keyQueue);
    }

    private static Object get(Object source, Queue<String> keyQueue) {
        if (keyQueue.isEmpty()) {
            return source;
        }

        // poll
        String key = keyQueue.poll();

        // key1.key2.key3
        if (source instanceof Map) {
            Map<String, String> map = (Map<String, String>) source;
            if (MapUtils.isEmpty(map)) {
                return null;
            }

            Object value = map.get(key);
            return get(value, keyQueue);
        }

        // key1.key2.key4[2]
        if (source instanceof Collection) {
            return null;
        }

        return null;
    }

}
