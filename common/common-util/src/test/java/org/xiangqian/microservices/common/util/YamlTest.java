package org.xiangqian.microservices.common.util;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author xiangqian
 * @date 20:42 2023/07/31
 */
public class YamlTest {

    @Test
    public void load() {
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.yml");
            Yaml yaml = new Yaml(inputStream);
            System.out.format("server: %s", yaml.getString("server")).println();
            System.out.format("server.port: %s", yaml.getInt("server.port")).println();
            System.out.format("server.servlet: %s", yaml.getString("server.servlet")).println();
            System.out.format("server.servlet.context-path: %s", yaml.getString("server.servlet.context-path")).println();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
