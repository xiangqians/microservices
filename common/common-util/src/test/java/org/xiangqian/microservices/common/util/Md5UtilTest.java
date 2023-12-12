package org.xiangqian.microservices.common.util;

import org.junit.Test;

/**
 * @author xiangqian
 * @date 20:26 2023/11/14
 */
public class Md5UtilTest {

    @Test
    public void test() {
        String data = "Hello, World!";

        String result = Md5Util.encryptHex(data);
        System.out.println(result);

        result = Md5Util.encryptHex0(data);
        System.out.println(result);

        // 65a8e27d8879283831b664bd8b7f0ad4
        // 65a8e27d8879283831b664bd8b7f0ad4
    }


}
