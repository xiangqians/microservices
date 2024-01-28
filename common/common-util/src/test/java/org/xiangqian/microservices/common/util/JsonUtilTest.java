package org.xiangqian.microservices.common.util;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiangqian
 * @date 21:14 2024/01/17
 */
public class JsonUtilTest {

    @Test
    public void serialize() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(8, 1f);
        map.put("data", LocalDate.now());
        map.put("time", LocalTime.now());
        map.put("dataTime", LocalDateTime.now());
        String json = JsonUtil.serializeAsString(map);
        System.out.println(json);
        json = JsonUtil.serializeAsPrettyString(map);
        System.out.println(json);
    }

}
