package com.ioc.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LinuxCondition implements Condition {

    /**
     * 匹配
     * @param context  上下文信息
     * @param metadata 注释信息
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取ioc使用的beanFactory
        final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        // 获取类加载器
        final ClassLoader classLoader = context.getClassLoader();

        // 获取当前环境信息
        final Environment environment = context.getEnvironment();

        // 获取bean定义的注册类
        final BeanDefinitionRegistry registry = context.getRegistry();

        final boolean person = registry.containsBeanDefinition("person");
        System.out.println("linux condition是否包含person:" + person);

        final String property = environment.getProperty("os.name");

        final boolean linux = property.contains("linux");
        if (linux){
            return true;
        }
        return false;
    }
}
