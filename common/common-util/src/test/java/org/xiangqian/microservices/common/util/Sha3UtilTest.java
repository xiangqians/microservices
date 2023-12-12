package org.xiangqian.microservices.common.util;

import org.junit.Test;

/**
 * @author xiangqian
 * @date 20:48 2023/11/14
 */
public class Sha3UtilTest {

    @Test
    public void testSha256() {
        String data = "Hello, World!";

        String result = Sha3Util.encrypt256Hex(data);
        System.out.println(result);

        // 1af17a664e3fa8e419b8ba05c2a173169df76162a5a286e0c405b460d478f7ef
    }

}
