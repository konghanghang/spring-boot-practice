package com.ioc;

import com.ioc.annotation.MainConfig;
import com.ioc.annotation.MainConfig2;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IocTest {

    @Test
    public void test02(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        final String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
        final Person bean = context.getBean(Person.class);
        final Person bean1 = context.getBean(Person.class);
        System.out.println(bean == bean1);
    }

    @Test
    public void test01(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        final String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
    }

}
