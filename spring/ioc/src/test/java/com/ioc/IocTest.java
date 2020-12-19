package com.ioc;

import com.ioc.annotation.MainConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IocTest {

    @Test
    public void test01(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        final String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
    }

}
