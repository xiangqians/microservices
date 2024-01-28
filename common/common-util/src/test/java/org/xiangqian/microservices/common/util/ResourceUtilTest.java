package org.xiangqian.microservices.common.util;

import org.junit.Test;
import org.springframework.core.io.Resource;

import java.util.Set;

/**
 * @author xiangqian
 * @date 20:53 2023/07/31
 */
public class ResourceUtilTest {

    @Test
    public void getClasses() throws Exception {
        // 获取 org.xiangqian.microservices.common.util 包下的类（不包括子包）
        Set<Class<?>> classes = ResourceUtil.getClasses("org.xiangqian.microservices.common.util");
        System.out.println(classes.size());
        classes.forEach(System.out::println);
        System.out.println();

        // 获取 org.xiangqian.microservices.common.util 包及其子包下的类
        classes = ResourceUtil.getClasses("org.xiangqian.microservices.common.util.**");
        System.out.println(classes.size());
        classes.forEach(System.out::println);
    }

    @Test
    public void getPkgs() throws Exception {
        // 获取 org.xiangqian.microservices.common.util 包名
        Set<String> pkgs = ResourceUtil.getPkgs("org.xiangqian.microservices.common.util");
        System.out.println(pkgs.size());
        pkgs.forEach(System.out::println);
        System.out.println();

        // 获取 org.xiangqian.microservices.common.util 包及其子包名
        pkgs = ResourceUtil.getPkgs("org.xiangqian.microservices.common.util.**");
        System.out.println(pkgs.size());
        pkgs.forEach(System.out::println);
    }

    @Test
    public void getResources() throws Exception {
        // 获取 META-INF/ 目录下所有 factories 文件（不包括子目录）
        Set<Resource> resources = ResourceUtil.getResources("META-INF/*.factories");
        System.out.println(resources.size());
        resources.forEach(System.out::println);
        System.out.println();

        // 获取 META-INF/ 目录及其子目录下所有 factories 文件
        resources = ResourceUtil.getResources("META-INF/**/*.factories");
        System.out.println(resources.size());
        resources.forEach(System.out::println);
    }

}
