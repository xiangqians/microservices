package org.xiangqian.microservices.common.util;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 资源扫描工具
 * <p>
 * 基于spring框架扫描包工具类下自定义扫描资源
 * See {@link org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider}
 *
 * @author xiangqian
 * @date 21:05 2022/09/07
 */
public class ResourceUtil {

    private static final Provider provider = new Provider();

    private static final Function<Resource, Class<?>> classResolver = resource -> {
        try {
            MetadataReader metadataReader = provider.getMetadataReader(resource);
            String className = metadataReader.getAnnotationMetadata().getClassName();
            return Class.forName(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    private static final Function<Resource, File> fileResolver = resource -> {
        throw new UnsupportedOperationException();
    };

    /**
     * 扫描包
     *
     * @param basePkgs 基础包集
     * @return
     * @throws IOException
     */
    public static Set<String> scanPkgs(String... basePkgs) throws IOException {
        Assert.isTrue(ArrayUtils.isNotEmpty(basePkgs), "basePkgs must not be null");
        return scan(HashSet::new, resource -> classResolver.apply(resource).getPackageName(), resolveBasePkgs(basePkgs));
    }

    /**
     * 扫描类
     *
     * @param basePkgs 基础包集
     * @return
     * @throws IOException
     */
    public static Set<Class<?>> scanClasses(String... basePkgs) throws IOException {
        Assert.isTrue(ArrayUtils.isNotEmpty(basePkgs), "basePkgs must not be null");
        return scan(HashSet::new, classResolver, resolveBasePkgs(basePkgs));
    }

    /**
     * 扫描资源
     *
     * @param supplier         <C> 提供者
     * @param resolver         {@link Resource} 解析器
     * @param locationPatterns 位置匹配集合
     * @param <T>
     * @param <C>
     * @return
     * @throws IOException
     */
    private static <T, C extends Collection<T>> C scan(Supplier<C> supplier, Function<Resource, T> resolver, String... locationPatterns) throws IOException {
        Assert.notNull(supplier, "supplier must not be null");
        Assert.notNull(resolver, "resolver must not be null");
        Assert.isTrue(ArrayUtils.isNotEmpty(locationPatterns), "locationPatterns must not be null");
        C c = supplier.get();
        for (String locationPattern : locationPatterns) {
            Resource[] resources = provider.getResources(locationPattern);
            for (int i = 0, len = resources.length; i < len; i++) {
                Resource resource = resources[i];
                if (resource.isReadable()) {
                    Optional.ofNullable(resolver.apply(resource)).ifPresent(c::add);
                }
            }
        }
        return c;
    }

    private static String[] resolveBasePkgs(String... basePkgs) {
        return Arrays.stream(basePkgs).map(ResourceUtil::resolveBasePkg).toArray(String[]::new);
    }

    /**
     * 解析basePkg为locationPattern
     *
     * @param basePkg
     * @return locationPattern
     */
    private static String resolveBasePkg(String basePkg) {
        String className = provider.getEnvironment().resolveRequiredPlaceholders(basePkg);
        Assert.notNull(className, "Class name must not be null");
        return String.format("classpath*:%s/**/*.class", className.replace('.', '/'));
    }

    private static class Provider implements EnvironmentCapable {
        @Getter
        private Environment environment;
        private ResourcePatternResolver resourcePatternResolver;
        private MetadataReaderFactory metadataReaderFactory;

        Provider() {
            this.environment = new StandardEnvironment();
            this.setResourceLoader(null);
        }

        public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
            if (resourceLoader instanceof ResourcePatternResolver) {
                resourcePatternResolver = (ResourcePatternResolver) resourceLoader;
            } else {
                resourcePatternResolver = Objects.nonNull(resourceLoader) ? new PathMatchingResourcePatternResolver(resourceLoader) : new PathMatchingResourcePatternResolver();
            }
            metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        }

        public MetadataReader getMetadataReader(String className) throws IOException {
            return metadataReaderFactory.getMetadataReader(className);
        }

        public MetadataReader getMetadataReader(Resource resource) throws IOException {
            return metadataReaderFactory.getMetadataReader(resource);
        }

        public Resource[] getResources(String locationPattern) throws IOException {
            return resourcePatternResolver.getResources(locationPattern);
        }
    }

}
