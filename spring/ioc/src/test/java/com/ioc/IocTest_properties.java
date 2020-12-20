package com.ioc;

import com.ioc.config.MainConfigOfProperties;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IocTest_properties {

    @Test
    public void test01(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfProperties.class);
        final String[] names = context.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
        final Object propertiesVo = context.getBean("propertiesVo");
        System.out.println(propertiesVo);
    }

}
