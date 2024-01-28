package org.xiangqian.microservices.common.util;

import org.junit.Test;

/**
 * @author xiangqian
 * @date 21:22 2024/01/02
 */
public class JsUtilTest {

    @Test
    public void test() {
        Object result = JsUtil.execute("sum(100, 1)", """
                function sum(i1, i2){
                    return i1 + i2
                }
                """);
        System.out.println(result.getClass());
        System.out.println(result);
    }

}
