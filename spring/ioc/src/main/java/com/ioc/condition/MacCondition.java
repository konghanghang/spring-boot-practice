package com.ioc.condition;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MacCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取当前环境信息
        final Environment environment = context.getEnvironment();

        final String property = environment.getProperty("os.name");

        // 获取bean定义的注册类
        final BeanDefinitionRegistry registry = context.getRegistry();

        final boolean person = registry.containsBeanDefinition("person");
        System.out.println("mac condition 是否包含person:" + person);

        final boolean mac = property.contains("Mac");
        if (mac){
            return true;
        }
        return false;
    }
}
