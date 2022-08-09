package org.microservices.gateway;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author xiangqian
 * @date 13:47 2022/04/06
 */
public class PathMatcherTest {

    public static void main(String[] args) {

        PathMatcher pathMatcher = new AntPathMatcher();

        String pattern = "user/**";
        String path = "user";
        System.out.println(String.format("isPattern=%s", pathMatcher.isPattern(pattern)));
        System.out.println(String.format("isPattern=%s", pathMatcher.isPattern(path)));
        System.out.println(String.format("ismMatch=%s", pathMatcher.match(pattern, path)));
        System.out.println(String.format("extractPathWithinPattern=%s", pathMatcher.extractPathWithinPattern(pattern, path)));


    }


}
