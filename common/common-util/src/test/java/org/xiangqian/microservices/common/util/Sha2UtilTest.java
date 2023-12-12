package org.xiangqian.microservices.common.util;

import org.junit.Test;

/**
 * @author xiangqian
 * @date 19:42 2023/11/14
 */
public class Sha2UtilTest {

    @Test
    public void testSha256() {
        String data = "Hello, World!";

        String result = Sha2Util.encrypt256Hex(data);
        System.out.println(result);

        result = Sha2Util.encrypt256Hex0(data);
        System.out.println(result);

        // dffd6021bb2bd5b0af676290809ec3a53191dd81c7f70a4b28688a362182986f
        // dffd6021bb2bd5b0af676290809ec3a53191dd81c7f70a4b28688a362182986f
    }

}
