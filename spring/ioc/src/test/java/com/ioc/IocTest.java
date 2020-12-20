package com.ioc;

import com.ioc.annotation.MainConfig;
import com.ioc.annotation.MainConfig2;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class IocTest {

    @Test
    public void testImport(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        final String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
        // 工厂bean调用getObject创建对象，
        final Object colorFactoryBean = context.getBean("colorFactoryBean");
        System.out.println("bean的类型：" + colorFactoryBean.getClass());

        // 获取工厂bean本身
        final Object colorFactory = context.getBean("&colorFactoryBean");
        System.out.println("bean的类型：" + colorFactory.getClass());
    }

    @Test
    public void test03(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        final String[] beanDefinitionNames = context.getBeanDefinitionNames();
        final ConfigurableEnvironment environment = context.getEnvironment();
        final String property = environment.getProperty("os.name");
        System.out.println(property);
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
    }

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
