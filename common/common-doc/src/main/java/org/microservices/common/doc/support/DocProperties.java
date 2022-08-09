package org.microservices.common.doc.support;

//import springfox.documentation.service.Contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * doc配置
 *
 * @author xiangqian
 * @date 15:41 2022/04/02
 */
public interface DocProperties {

    String title();

    String description();

    Contact contact();

    String version();

    String[] basePackages();

    String[] matchPaths();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }

}
