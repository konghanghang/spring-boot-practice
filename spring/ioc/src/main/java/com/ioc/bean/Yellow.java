package com.ioc.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

public class Yellow implements ApplicationContextAware, BeanNameAware, EmbeddedValueResolverAware {
    @Override
    public void setBeanName(String name) {
        System.out.println("bean名称：" + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("applicationContext:" + applicationContext);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        final String s = resolver.resolveStringValue("你好：${os.name}");
        System.out.println(s);
    }
}
