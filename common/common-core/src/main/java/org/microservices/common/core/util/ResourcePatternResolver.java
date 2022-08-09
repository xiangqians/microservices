package org.microservices.common.core.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.microservices.common.core.support.AntPathMatcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * 资源解析器
 * <p>
 * 参考：
 * {@link org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider}
 * {@link org.springframework.core.io.support.PathMatchingResourcePatternResolver}
 *
 * @author xiangqian
 * @date 08:19 2020/06/10
 */
public class ResourcePatternResolver {

    private static final org.springframework.core.io.support.ResourcePatternResolver RESOURCE_PATTERN_RESOLVER;
    private static final MetadataReaderFactory METADATA_READER_FACTORY;

    static {
        RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
        METADATA_READER_FACTORY = new CachingMetadataReaderFactory((ResourceLoader) null);
    }

    private static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
    public static final String CLASS_RESOURCE_PATTERN = "**/*.class";
    public static final String YML_RESOURCE_PATTERN = "**/*.yml";
    public static final String YAML_RESOURCE_PATTERN = "**/*.yaml";
    public static final String PROPERTIES_RESOURCE_PATTERN = "**/*.properties";

    /**
     * 获取包路径
     *
     * @param basePackages
     * @return
     * @throws IOException
     */
    public static Set<String> getPackageSet(String... basePackages) throws IOException {
        if (ArrayUtils.isEmpty(basePackages)) {
            return Collections.emptySet();
        }

        Set<String> set = new HashSet<>();
        for (String basePackage : basePackages) {
            if (StringUtils.isEmpty(basePackage = StringUtils.trim(basePackage))) {
                continue;
            }
            AntPathMatcher antPathMatcher = new AntPathMatcher(basePackage);
            getResources(convertBasePackageToResourcePattern(basePackage), resource -> {
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = METADATA_READER_FACTORY.getMetadataReader(resource);
                        String className = metadataReader.getAnnotationMetadata().getClassName();
                        String result = antPathMatcher.extractPathMatchingPattern(className);
                        if (result != null) {
                            set.add(result);
                        }
                    } catch (IOException e) {
                    }
                }
                return false;
            });
        }
        return set;
    }

    /**
     * get class set
     *
     * @param candidateFunction 候选组件
     * @param basePackages      基础包名
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> Set<Class<T>> getClassSet(Function<Class<T>, Boolean> candidateFunction, String... basePackages) throws IOException {
        if (ArrayUtils.isEmpty(basePackages)) {
            return Collections.emptySet();
        }

        Set<Class<T>> set = new HashSet<>();
        for (String basePackage : basePackages) {
            if (StringUtils.isEmpty(basePackage = StringUtils.trim(basePackage))) {
                continue;
            }
            getResources(convertBasePackageToResourcePattern(basePackage), resource -> {
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = METADATA_READER_FACTORY.getMetadataReader(resource);
                        // candidate
                        String className = metadataReader.getAnnotationMetadata().getClassName();
                        if (ObjectUtils.allNotNull(candidateFunction)) {
                            Class<T> tClass = (Class<T>) Class.forName(className);
                            if (BooleanUtils.isTrue(candidateFunction.apply(tClass))) {
                                set.add(tClass);
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                    }
                }
                return false;
            });
        }
        return set;
    }

    /**
     * get resources
     *
     * @param resourcePattern   see:
     *                          {@link ResourcePatternResolver#CLASS_RESOURCE_PATTERN}
     *                          {@link ResourcePatternResolver#YML_RESOURCE_PATTERN}
     *                          {@link ResourcePatternResolver#YAML_RESOURCE_PATTERN}
     *                          {@link ResourcePatternResolver#PROPERTIES_RESOURCE_PATTERN}
     *                          other
     * @param candidateFunction 候选器
     * @return
     * @throws IOException
     */
    public static List<Resource> getResources(String resourcePattern, Function<Resource, Boolean> candidateFunction) throws IOException {
        String locationPattern = CLASSPATH_ALL_URL_PREFIX + resourcePattern;
        Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(locationPattern);
        if (Objects.isNull(candidateFunction)) {
            return Lists.newArrayList(resources);
        }

        List<Resource> list = null;
        for (Resource resource : resources) {
            if (BooleanUtils.toBoolean(candidateFunction.apply(resource))) {
                if (Objects.isNull(list)) {
                    list = new ArrayList<>();
                }
                list.add(resource);
            }
        }
        return list;
    }

    private static String convertBasePackageToResourcePattern(String basePackage) {
        String resourcePattern = String.format("%s/%s", basePackage.replace('.', '/'), CLASS_RESOURCE_PATTERN);
        return resourcePattern;
    }

}
