package org.xiangqian.microservices.common.util;

import com.google.common.base.CaseFormat;
import org.junit.Test;

/**
 * @author xiangqian
 * @date 20:11 2023/10/19
 */
public class NamingConvUtilTest {

    @Test
    public void test() {
        System.out.println(NamingConvUtil.lowerCamelToUpperCamel("testData")); // TestData
        System.out.println(NamingConvUtil.lowerCamelToUnderline("testData")); // test_data

        System.out.println(NamingConvUtil.upperCamelToLowerCamel("TestData")); // testData
        System.out.println(NamingConvUtil.upperCamelToUnderline("TestData")); // test_data

        System.out.println(NamingConvUtil.underlineToLowerCamel("test_data")); // testData
        System.out.println(NamingConvUtil.underlineToUpperCamel("test_data")); // TestData
    }

    @Test
    public void test0() {
        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data")); // testData
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData")); // test-data
    }

}
