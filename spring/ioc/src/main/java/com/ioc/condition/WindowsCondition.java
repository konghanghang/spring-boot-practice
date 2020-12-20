package com.ioc.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class WindowsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 获取当前环境信息
        final Environment environment = context.getEnvironment();

        final String property = environment.getProperty("os.name");

        final boolean windows = property.contains("Windows");
        if (windows){
            return true;
        }
        return false;
    }
}
