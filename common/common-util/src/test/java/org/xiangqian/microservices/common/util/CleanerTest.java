package org.xiangqian.microservices.common.util;

import org.xiangqian.microservices.common.util.Cleanable;
import org.xiangqian.microservices.common.util.Cleaner;

/**
 * @author xiangqian
 * @date 20:28 2023/08/14
 */
public class CleanerTest {

    public static void main(String[] args) {
        Cleanable cleanable = () -> System.out.println("++++++清除++++++");
        Cleaner.add(cleanable);
        System.gc();
    }

}
