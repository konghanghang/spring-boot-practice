package com.ioc;

import com.ioc.annotation.MainConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {
        final ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("bean.xml");
        final String[] beanDefinitionNames = classPathXmlApplicationContext.getBeanDefinitionNames();
        final Person person = (Person) classPathXmlApplicationContext.getBean("person");
        System.out.println(person.toString());
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        classPathXmlApplicationContext.close();

        final AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        final Person bean = annotationConfigApplicationContext.getBean(Person.class);
        System.out.println(bean.toString());
        final String[] beanDefinitionNames1 = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String s : beanDefinitionNames1) {
            System.out.println(s);
        }
    }

}
