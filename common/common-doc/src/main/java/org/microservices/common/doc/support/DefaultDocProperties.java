package org.microservices.common.doc.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author xiangqian
 * @date 16:06 2022/04/02
 */
@Slf4j
public class DefaultDocProperties implements DocProperties {

    @Value("${spring.application.name}")
    protected String name;

    @Value("${microservices.application.version}")
    protected String version;

    @Override
    public String title() {
        return String.format("%s API Documentation", name);
    }

    @Override
    public String description() {
        return String.format("%s API Documentation", name);
    }

    @Override
    public Contact contact() {
        Contact contact = new Contact();
        contact.setEmail("xiangqian@unknown.org");
        contact.setName("xiangqian");
        contact.setUrl("https://github.com/xiangqians");
        return contact;
    }

    @Override
    public String version() {
        return String.format("v%s", version);
    }

    @Override
    public String[] basePackages() {
        return new String[]{"org.microservices.**.controller"};
    }

    @Override
    public String[] matchPaths() {
        return new String[]{"/**"};
    }

}
