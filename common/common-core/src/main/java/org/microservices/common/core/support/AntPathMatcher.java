package org.microservices.common.core.support;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiangqian
 * @date 22:41:49 2022/05/01
 */
public class AntPathMatcher {

    private Pattern pattern;

    public AntPathMatcher(String pattern) {
        this(pattern, "\\.");
    }

    public AntPathMatcher(String pattern, String pathSeparator) {
        String[] array = pattern.split(pathSeparator);
        for (int i = 0, length = array.length; i < length; i++) {
            String element = array[i];
            if (element.contains("**")) {
                array[i] = element.replace("**", "(.*)");
            } else if (element.contains("*")) {
                array[i] = element.replace("*", "([^.]*)");
            }
        }
        String regex = StringUtils.join(array, "\\.");
        this.pattern = Pattern.compile(regex);
    }

    public boolean match(String path) {
        return pattern.matcher(path).matches();
    }

    public boolean find(String path) {
        return pattern.matcher(path).find();
    }

    /**
     * 提取匹配模式的路径
     *
     * @param path
     * @return
     */
    public String extractPathMatchingPattern(String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

}
