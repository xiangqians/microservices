package org.microservices.common.cache.support.ehcache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.ConfigurationHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.microservices.common.core.util.ResourceResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.*;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

/**
 * Ehcache缓存支持
 *
 * @author xiangqian
 * @date 19:22 2022/04/07
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "microservices.cache.type", havingValue = "ehcache")
public class EhcacheSupport {

    public static final String ARRAY_KEY_GENERATOR = "arrayKeyGenerator";

    // KEY生成器
    @Bean
    public KeyGenerator arrayKeyGenerator() {
        return (Object obj, Method method, Object... params) -> String.format("%s.%s.", method.getName(), Arrays.toString(params));
    }

    /**
     * 查看源码：
     * {@link AbstractCacheManager#getCache(java.lang.String)}
     * {@link CacheAspectSupport.CacheOperationContexts#CacheOperationContexts(java.util.Collection, java.lang.reflect.Method, java.lang.Object[], java.lang.Object, java.lang.Class)}
     * {@link CacheOperation} 实现类有：{@link CacheEvictOperation}、{@link CachePutOperation}、{@link CacheableOperation}
     * {@link CacheAspectSupport#execute(org.springframework.cache.interceptor.CacheOperationInvoker, java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}
     * {@link CacheAspectSupport#execute(org.springframework.cache.interceptor.CacheOperationInvoker, java.lang.reflect.Method, org.springframework.cache.interceptor.CacheAspectSupport.CacheOperationContexts)}
     * {@link CacheAspectSupport#processCacheEvicts(java.util.Collection, boolean, java.lang.Object)}
     * {@link CacheAspectSupport#performCacheEvict(org.springframework.cache.interceptor.CacheAspectSupport.CacheOperationContext, org.springframework.cache.interceptor.CacheEvictOperation, java.lang.Object)}
     * {@link CacheAspectSupport.CacheOperationContext#generateKey(java.lang.Object)}
     * {@link AbstractCacheInvoker#doEvict(org.springframework.cache.Cache, java.lang.Object, boolean)}
     * <p>
     * org.springframework.cache.interceptor.AbstractCacheResolver.resolveCaches
     * org.springframework.cache.interceptor.SimpleCacheResolver.resolveCaches
     * org.springframework.cache.interceptor.CacheAspectSupport.getCaches
     * org.springframework.cache.interceptor.CacheAspectSupport.CacheOperationContext.CacheOperationContext
     * org.springframework.cache.interceptor.CacheAspectSupport.getOperationContext
     * org.springframework.cache.interceptor.CacheAspectSupport.CacheOperationContexts
     * org.springframework.cache.interceptor.CacheAspectSupport.execute(org.springframework.cache.interceptor.CacheOperationInvoker, java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Bean
    public CacheManager ehCacheCacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean) {
        return new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());
//        return new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject()) {
//            @Override
//            protected Cache getMissingCache(String name) {
//                net.sf.ehcache.CacheManager cacheManager = this.getCacheManager();
//                Assert.state(cacheManager != null, "No CacheManager set");
//                Ehcache ehcache = cacheManager.getEhcache("jjr-iot-default");
//                return ehcache != null ? new EhCacheCache(ehcache) : null;
//            }
//        };
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() throws NoSuchFieldException {
        EhCacheManagerFactoryBean bean = new EnhancedEhCacheManagerFactoryBean();
        bean.setConfigLocation(new ClassPathResource("cache/ehcache.xml"));
        bean.setShared(true);
        return bean;
    }

    /**
     * 动态生成默认 <cache name="xxx"></cache>
     * <p>
     * 查看源码：
     * {@link ConfigurationHelper#createCaches()}
     * {@link ConfigurationHelper#ConfigurationHelper(net.sf.ehcache.CacheManager, net.sf.ehcache.config.Configuration)}
     * {@link net.sf.ehcache.CacheManager#init(net.sf.ehcache.config.Configuration, java.lang.String, java.net.URL, java.io.InputStream)}
     * {@link EhCacheManagerFactoryBean#afterPropertiesSet()}
     *
     * @return
     */
    public static class EnhancedEhCacheManagerFactoryBean extends EhCacheManagerFactoryBean {

        private Field cacheManagerNameField;
        private Field configLocationField;
        private Field sharedField;
        private Field cacheManagerField;
        private Field acceptExistingField;
        private Field locallyManagedField;

        public EnhancedEhCacheManagerFactoryBean() throws NoSuchFieldException {
            cacheManagerNameField = EhCacheManagerFactoryBean.class.getDeclaredField("cacheManagerName");
            cacheManagerNameField.setAccessible(true);
            configLocationField = EhCacheManagerFactoryBean.class.getDeclaredField("configLocation");
            configLocationField.setAccessible(true);
            sharedField = EhCacheManagerFactoryBean.class.getDeclaredField("shared");
            sharedField.setAccessible(true);
            cacheManagerField = EhCacheManagerFactoryBean.class.getDeclaredField("cacheManager");
            cacheManagerField.setAccessible(true);
            acceptExistingField = EhCacheManagerFactoryBean.class.getDeclaredField("acceptExisting");
            acceptExistingField.setAccessible(true);
            locallyManagedField = EhCacheManagerFactoryBean.class.getDeclaredField("locallyManaged");
            locallyManagedField.setAccessible(true);
        }

        private InputStream createNodeIfAbsent() throws Exception {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(configLocation().getInputStream());
            document.getDocumentElement().normalize();

            // cache node list
            NodeList cacheNodes = document.getElementsByTagName("cache");
            Set<String> cacheNameSet = new HashSet<>();
            Element defaultElement = null;
            for (int i = 0, length = cacheNodes.getLength(); i < length; i++) {
                Element element = (Element) cacheNodes.item(i);
                String name = element.getAttribute("name");
                cacheNameSet.add(name);
                if ("CACHE_DEFAULT".equals(name)) {
                    defaultElement = element;
                }
            }

            // 获取dao层所有class中定义的cache name
            Set<String> prepareCacheNameSet = new HashSet<>();
            ResourceResolver.getClassSet(candidateClass -> {
                Method[] methods = candidateClass.getMethods();
                for (Method method : methods) {
                    String[] cacheNames = null;
                    if (method.isAnnotationPresent(Cacheable.class)) {
                        cacheNames = method.getAnnotation(Cacheable.class).cacheNames();
                    } else if (method.isAnnotationPresent(CacheEvict.class)) {
                        cacheNames = method.getAnnotation(Cacheable.class).cacheNames();
                    }
                    Optional.ofNullable(cacheNames).filter(ArrayUtils::isNotEmpty).ifPresent(strings -> Stream.of(strings).forEach(prepareCacheNameSet::add));
                }
                return false;
            }, "org.microservices");

            // 去重并创建cache节点
            cacheNameSet.forEach(prepareCacheNameSet::remove);
            Node ehcacheNode = document.getElementsByTagName("ehcache").item(0);
            if (!CollectionUtils.isEmpty(prepareCacheNameSet)) {
                for (String prepareCacheName : prepareCacheNameSet) {
                    Element cloneElement = (Element) defaultElement.cloneNode(true);
                    cloneElement.setAttribute("name", prepareCacheName);
                    ehcacheNode.appendChild(cloneElement);
                }
            }

            // write the updated document to bytes
            document.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            transformer.transform(source, new StreamResult(byteArrayOutputStream));
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return byteArrayInputStream;
        }

        @Override
        public void afterPropertiesSet() throws CacheException {
            if (logger.isDebugEnabled()) {
                logger.debug("Initializing EhCache CacheManager" + (cacheManagerName() != null ? " '" + cacheManagerName() + "'" : ""));
            }

            net.sf.ehcache.config.Configuration configuration = null;
            try {
                configuration = ConfigurationFactory.parseConfiguration(createNodeIfAbsent());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (cacheManagerName() != null) {
                configuration.setName(cacheManagerName());
            }

            if (shared()) {
                cacheManager(net.sf.ehcache.CacheManager.create(configuration));
            } else if (acceptExisting()) {
                synchronized (net.sf.ehcache.CacheManager.class) {
                    cacheManager(net.sf.ehcache.CacheManager.getCacheManager(cacheManagerName()));
                    if (Objects.isNull(cacheManager())) {
                        cacheManager(new net.sf.ehcache.CacheManager(configuration));
                    } else {
                        locallyManaged(false);
                    }
                }
            } else {
                cacheManager(new net.sf.ehcache.CacheManager(configuration));
            }
        }

        private void locallyManaged(boolean locallyManaged) {
            try {
                locallyManagedField.set(this, locallyManaged);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean acceptExisting() {
            try {
                return (boolean) acceptExistingField.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private net.sf.ehcache.CacheManager cacheManager() {
            try {
                return (net.sf.ehcache.CacheManager) cacheManagerField.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private void cacheManager(net.sf.ehcache.CacheManager cacheManager) {
            try {
                cacheManagerField.set(this, cacheManager);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean shared() {
            try {
                return (boolean) sharedField.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private Resource configLocation() {
            try {
                return (Resource) configLocationField.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private String cacheManagerName() {
            try {
                return (String) cacheManagerNameField.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
