package org.microservices.common.web.support;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.microservices.common.core.env.CoreEnvironmentAware;
import org.microservices.common.web.annotation.FeignServiceImpl;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author xiangqian
 * @date 22:06 2022/03/27
 */
@Slf4j
public class FeignServiceBeanDefinitionRegistryPostProcessor extends CoreEnvironmentAware implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            if (!(beanDefinition instanceof GenericBeanDefinition)) {
                continue;
            }

            // 加载beanClass
            String beanClassName = beanDefinition.getBeanClassName();
            Class<?> beanClass = null;
            try {
                beanClass = Class.forName(beanClassName);
            } catch (Exception e) {
                log.error(String.format("加载beanClass(beanClassName=%s)异常", beanClassName), e);
                throw new RuntimeException(e);
            }

            //
            if (!beanClass.isAnnotationPresent(FeignServiceImpl.class)) {
                continue;
            }

            // 使用ASM生成ApiServiceImpl增强类
            Class<?> enhancedBeanClass = null;
            try {
                enhancedBeanClass = enhancedFeignServiceImplClass(beanClass);
            } catch (Exception e) {
                log.error(String.format("使用ASM生成FeignServiceImpl(beanClassName=%s)增强类异常", beanClassName), e);
                throw new RuntimeException(e);
            }

            // 移除原来定义的BeanDefinition
            beanDefinitionRegistry.removeBeanDefinition(beanDefinitionName);

            // 构建BeanDefinition
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(enhancedBeanClass);

            // 注册BeanDefinition
            beanDefinitionRegistry.registerBeanDefinition(beanDefinitionName, beanDefinitionBuilder.getBeanDefinition());
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    /**
     * 使用ASM生成 FeignServiceImpl 增强类
     *
     * @param clazz
     * @return
     */
    private Class<?> enhancedFeignServiceImplClass(Class<?> clazz) throws Exception {
        String superClass = clazz.getName().replace(".", "/");
        String thisClass = String.format("%s/Enhanced%s", superClass.substring(0, superClass.lastIndexOf("/")), clazz.getSimpleName());
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(SourceVersion.latestSupported().getValue(), Opcodes.ACC_PUBLIC, thisClass, null, superClass, null);

        // 为类添加 FeignServiceImpl 注解
        AnnotationVisitor feignServiceImplAnnotation = classWriter.visitAnnotation(String.format("L%s;", FeignServiceImpl.class.getName().replace(".", "/")), true);
        feignServiceImplAnnotation.visitEnd();

        // 获取源类RequestMapping注解
        Set<String> value = new HashSet<>(1);
        Function<Class<?>, Boolean> function = c -> {
            if (c.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = c.getAnnotation(RequestMapping.class);
                String[] array = requestMapping.value();
                for (String v : array) {
                    value.add(getFeignBasePath() + v);
                }
                return true;
            }
            return false;
        };
        // super class
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            if (function.apply(c)) {
                break;
            }
        }
        // interface
        if (CollectionUtils.isEmpty(value)) {
            for (Class<?> c : clazz.getInterfaces()) {
                if (function.apply(c)) {
                    break;
                }
            }
        }
        // default
        if (CollectionUtils.isEmpty(value)) {
            value.add(getFeignBasePath());
        }

        // 为类添加RequestMapping注解
        AnnotationVisitor requestMappingAnnotation = classWriter.visitAnnotation(String.format("L%s;", RequestMapping.class.getName().replace(".", "/")), true);
        AnnotationVisitor valueArrayAnnotation = requestMappingAnnotation.visitArray("value");
        for (String v : value) {
            valueArrayAnnotation.visit("value", v);
        }
        valueArrayAnnotation.visitEnd();
        requestMappingAnnotation.visitEnd();

        // 生成默认的构造方法
        MethodVisitor mw = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mw.visitVarInsn(Opcodes.ALOAD, 0); // 调用visitVarInsn方法，生成aload指令， 将第0个本地变量（也就是this）压入操作数栈。
        mw.visitMethodInsn(Opcodes.INVOKESPECIAL, superClass, "<init>", "()V");
        mw.visitInsn(Opcodes.RETURN);
        mw.visitMaxs(1, 1);
        mw.visitEnd();

        // 获取生成的class文件对应的字节数组
        byte[] bytecode = classWriter.toByteArray();

        // 使用类加载器加载类的字节数组
        ASMClassLoader asmClassLoader = new ASMClassLoader();
//        printClassLoaderInfo(asmClassLoader.getClass());
        Class<?> asmClass = asmClassLoader.asmDefineClass(thisClass.replace("/", "."), bytecode, 0, bytecode.length);
//        printClassLoaderInfo(asmClass);
        asmClassLoader = null; // 将类加载器引用指向null
        return asmClass;
    }


    private void printClassLoaderInfo(Class<?> c) {
        StringBuilder builder = new StringBuilder();
        ClassLoader classLoader = c.getClassLoader();
        while (classLoader != null) {
            builder.append("\t\t\t").append("- ").append(classLoader).append("\n");
            classLoader = classLoader.getParent();
        }
        builder.append("\t\t\t").append("- ").append(classLoader).append("\n");
        log.debug("'{}' ClassLoader: \n{}", c, builder);
    }


    @Getter
    public static enum SourceVersion {
        RELEASE_5(49),
        RELEASE_6(50),
        RELEASE_7(51),
        RELEASE_8(52),
        RELEASE_9(53),
        RELEASE_10(54),
        RELEASE_11(55),
        RELEASE_12(56),
        RELEASE_13(57),
        RELEASE_14(58),
        RELEASE_15(59),
        RELEASE_16(60),
        RELEASE_17(61),
        RELEASE_18(62),
        ;

        /**
         * {@link Opcodes#V12}
         */
        private final int value;

        private static final SourceVersion latestSupported = getLatestSupported();

        SourceVersion(int value) {
            this.value = value;
        }

        public static SourceVersion latestSupported() {
            return latestSupported;
        }

        private static SourceVersion getLatestSupported() {
            try {
                String specVersion = System.getProperty("java.specification.version");

                switch (specVersion) {
                    case "12":
                        return RELEASE_12;
                    case "11":
                        return RELEASE_11;
                    case "10":
                        return RELEASE_10;
                    case "9":
                        return RELEASE_9;
                    case "1.8":
                        return RELEASE_8;
                    case "1.7":
                        return RELEASE_7;
                    case "1.6":
                        return RELEASE_6;
                }
            } catch (SecurityException se) {
            }
            return RELEASE_5;
        }
    }

    public static class ASMClassLoader extends ClassLoader {

        public ASMClassLoader() {
            super(Thread.currentThread().getContextClassLoader());
        }

        public final Class<?> asmDefineClass(String name, byte[] b, int off, int len) throws ClassFormatError {
            return defineClass(name, b, off, len, null);
        }
    }

}
