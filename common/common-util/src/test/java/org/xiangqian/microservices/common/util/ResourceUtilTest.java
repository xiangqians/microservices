package org.xiangqian.microservices.common.util;

import org.junit.Test;
import org.xiangqian.microservices.common.util.ResourceUtil;

import java.util.Set;

/**
 * @author xiangqian
 * @date 20:53 2023/07/31
 */
public class ResourceUtilTest {

    @Test
    public void scanClasses() throws Exception {
        Set<Class<?>> classes = ResourceUtil.scanClasses("org.xiangqian.microservices.common.util.crc");
        classes.forEach(System.out::println);
    }

    @Test
    public void scanPkgs() throws Exception {
        Set<String> pkgs = ResourceUtil.scanPkgs("org.xiangqian.microservices.common");
        pkgs.forEach(System.out::println);
    }

}
