package org.xiangqian.microservices.common.util;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author xiangqian
 * @date 19:43 2024/01/25
 */
public class Base64UtilTest {

    @Test
    public void bytes() {
        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);

        // 编码
        String encodedData = Base64Util.Bytes.encodeToString(data);
        System.out.println(encodedData);

        // 解码
        data = Base64Util.Bytes.decode(encodedData);
        System.out.println(new String(data));
    }

}
