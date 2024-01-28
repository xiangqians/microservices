package org.xiangqian.microservices.common.util;

import org.junit.Test;

/**
 * @author xiangqian
 * @date 20:26 2023/11/14
 */
public class Md5UtilTest {

    @Test
    public void encrypt() {
        String data = "Hello, World!";
        String result = Md5Util.encryptHex(data);
        System.out.println(result);
    }

}
