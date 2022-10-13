package org.microservices.common.doc;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.microservices.common.auth.support.PermitAuthorizationRequest;
import org.microservices.common.auth.support.ResourceMatchPattern;
import org.microservices.common.core.util.ResourceResolver;
import org.microservices.common.core.web.ResourceHandlerRegistrar;
import org.microservices.common.doc.support.*;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Set;

/**
 * @author xiangqian
 * @date 11:57 2022/04/02
 */
@Slf4j
@Configuration
public class DocAutoConfiguration {

    // Authorization请求头名称
    private final String HEADER_AUTHORIZATION = "Authorization";

    @Bean
    @ConditionalOnMissingBean
    public DocProperties defaultDocProperties() {
        return new DefaultDocProperties();
    }

    @Bean
    public PermitAuthorizationRequest docPermitAuthorizationRequest() {
        return () -> Set.of(new ResourceMatchPattern("/webjars/**",
                "/v3/**",
                // swagger
                "/swagger-ui.html/**",
                "/swagger-ui/**",
                // knife4j
                "/doc.html/**"));
    }

    @Bean
    public GroupedOpenApi defaultGroupedOpenApi(DocProperties docProperties) throws IOException {
        Set<String> set = ResourceResolver.getPackageSet(docProperties.basePackages());
        return GroupedOpenApi.builder()
                // 组名称
                .group("default")

                // 基于接口路由扫描
                .pathsToMatch(docProperties.matchPaths())

                // 添加操作定制器
//                .addOperationCustomizer((operation, handlerMethod) -> {
//                    // 添加安全项，Authorization请求头
//                    operation.addSecurityItem(new SecurityRequirement().addList(HEADER_AUTHORIZATION));
//                    return operation;
//                })

                // 指定扫描API路径
                .packagesToScan(set.toArray(new String[set.size()]))

                .build();
    }

    @Bean
    public ParameterCustomizer customParameterCustomizer() {
        return new ParameterCustomizerImpl();
    }

    @Bean
    public ModelConverter modelConverter() {
        return new ModelConverterImpl();
    }

    @Bean
    public OpenAPI openAPI(DocProperties docProperties) {
        return new OpenAPI()
//                .components(components())
                .info(info(docProperties))
                .externalDocs(new ExternalDocumentation().description("SpringDoc Full Documentation").url("https://springdoc.org/"));
    }

    private Components components() {
        Components components = new Components();
        components
                // 添加安全方案（swagger-ui.html右上角统一安全认证项）
                .addSecuritySchemes(HEADER_AUTHORIZATION, new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .scheme("basic")
                        .name(HEADER_AUTHORIZATION).in(SecurityScheme.In.HEADER)
                        .description("--header 'Authorization: Bearer xxx'"))
        ;
        return components;
    }

    private Info info(DocProperties docProperties) {
        Contact contact = new Contact();
        contact.setEmail(docProperties.contact().getEmail());
        contact.setName(docProperties.contact().getName());
        contact.setUrl(docProperties.contact().getUrl());
        return new Info()
                .title(docProperties.title())
                .version(docProperties.version())
                .contact(contact)
                .description(docProperties.description())

                // https://www.gnu.org
                // https://www.gnu.org/licenses/gpl-3.0.en.html
                .license(new License().name("GPL-3.0").url("https://opensource.org/licenses/GPL-3.0"));
    }

    /**
     * 配置swagger静态资源访问
     * 解决doc.html或者swagger-ui.html 404问题
     *
     * @return
     */
    @Bean
    public ResourceHandlerRegistrar docResourceHandlerRegistrar() {
        return registry -> {
            // knife4j-ui & swagger-ui
            registry.addResourceHandler("swagger-ui.html", "doc.html").addResourceLocations("classpath:/META-INF/resources/");

            // webjars
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        };
    }


    ///////////////////

//    @Bean
//    public OpenAPI openAPI() {
//        var authUrl = String.format("%s/realms/%s/protocol/openid-connect", "authServer", "realm");
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("spring_oauth", new SecurityScheme()
//                                .type(SecurityScheme.Type.OAUTH2)
//                                .description("Oauth2 flow")
//                                .flows(new OAuthFlows()
//                                        .authorizationCode(new OAuthFlow()
//                                                .authorizationUrl(authUrl + "/auth")
//                                                .refreshUrl(authUrl + "/token")
//                                                .tokenUrl(authUrl + "/token")
//                                                .scopes(new Scopes())
//                                        )))
//                        .addSecuritySchemes("api_key", new SecurityScheme()
//                                .type(SecurityScheme.Type.APIKEY)
//                                .description("Api Key access")
//                                .in(SecurityScheme.In.HEADER)
//                                .name("API-KEY")
//                        )
//                        .addParameters("Version", new Parameter()
//                                .in("header")
//                                .name("Version")
//                                .schema(new StringSchema())
//                                .required(false)))
//                .security(Arrays.asList(
//                        new SecurityRequirement().addList("spring_oauth"),
//                        new SecurityRequirement().addList("api_key")))
//
//                .info(new Info()
//                        .title("Your API")
//                        .description("Your API")
//                        .version("2022.6")
//                        .contact(new Contact()
//                                .name("Example Inc")
//                                .url("https://www.example.com/")
//                                .email("developer@example.com")));
//    }

}
