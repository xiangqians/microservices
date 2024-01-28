package org.xiangqian.microservices.common.util;

import org.junit.Test;
import org.xiangqian.microservices.common.util.sha.Sha2Util;
import org.xiangqian.microservices.common.util.sha.Sha3Util;

/**
 * @author xiangqian
 * @date 19:42 2023/11/14
 */
public class ShaUtilTest {

    @Test
    public void sha2_256() {
        String data = "Hello, World!";

        String result = Sha2Util.encrypt256Hex(data);
        System.out.println(result);

        result = Sha2Util.encrypt256Hex0(data);
        System.out.println(result);

        // dffd6021bb2bd5b0af676290809ec3a53191dd81c7f70a4b28688a362182986f
        // dffd6021bb2bd5b0af676290809ec3a53191dd81c7f70a4b28688a362182986f
    }


    @Test
    public void sha3_256() {
        String data = "Hello, World!";

        String result = Sha3Util.encrypt256Hex(data);
        System.out.println(result);

        // 1af17a664e3fa8e419b8ba05c2a173169df76162a5a286e0c405b460d478f7ef
    }

}
