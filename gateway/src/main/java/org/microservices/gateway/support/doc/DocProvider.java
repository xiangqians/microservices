//package org.microservices.gateway.support.doc;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//import springfox.documentation.swagger.web.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * @author xiangqian
// * @date 16:38 2022/04/02
// */
//@Slf4j
//@Primary
//@RestController
//@RequestMapping("/swagger-resources")
//public class DocProvider implements SwaggerResourcesProvider {
//
//    // swagger2默认url
//    private static final String SWAGGER2_URL = "/v2/api-docs";
//
//    @Value("${spring.application.name}")
//    private String name;
//
//    @Autowired
//    private DiscoveryClient discoveryClient;
//
//    @Override
//    public List<SwaggerResource> get() {
//        // 接口资源列表
//        List<SwaggerResource> resources = new ArrayList<>();
//
//        // service set
//        Set<String> serviceSet = discoveryClient.getServices().stream().filter(service -> !name.equals(service)).collect(Collectors.toSet());
//        serviceSet.forEach(service -> {
//            SwaggerResource swaggerResource = new SwaggerResource();
//            swaggerResource.setUrl(String.format("/%s%s", service, SWAGGER2_URL));
//            swaggerResource.setName(service);
//            resources.add(swaggerResource);
//        });
//
//        //
//        return resources;
//    }
//
//    @Autowired(required = false)
//    private SecurityConfiguration securityConfiguration;
//
//    @Autowired(required = false)
//    private UiConfiguration uiConfiguration;
//
//    @Autowired
//    private SwaggerResourcesProvider swaggerResourcesProvider;
//
//    @GetMapping
//    public Mono<ResponseEntity> swaggerResources() {
//        return Mono.just((new ResponseEntity<>(swaggerResourcesProvider.get(), HttpStatus.OK)));
//    }
//
//    @GetMapping("/configuration/security")
//    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
//        return Mono.just(new ResponseEntity<>(Optional.ofNullable(securityConfiguration).orElse(SecurityConfigurationBuilder.builder().build()), HttpStatus.OK));
//    }
//
//    @GetMapping("/configuration/ui")
//    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
//        return Mono.just(new ResponseEntity<>(Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK));
//    }
//
//}
