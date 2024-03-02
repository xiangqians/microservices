package org.xiangqian.microservices.common.util;

import org.junit.Test;

import java.time.Duration;

/**
 * @author xiangqian
 * @date 11:59 2024/02/27
 */
public class DurationUtilTest {

    @Test
    public void test() {
        Duration duration = DurationUtil.parse("1h1m1s");
        System.out.println(duration);
        System.out.println(duration.getSeconds());
    }

}
