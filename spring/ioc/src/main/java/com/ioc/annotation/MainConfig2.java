package com.ioc.annotation;

import com.ioc.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MainConfig2 {

    /**
     * singleton 单实例（默认值） ioc容器启动会去调用方法创建对象放到ioc容器中，以后每次使用都去ioc容器中获取
     * prototype 多实例         ioc容器启动不会去调用方法创建对象，每次使用时才会调用方法获取对象，每次都是一个新对象
     * @return
     */
    @Scope
    @Bean
    public Person person(){
        return new Person("haha", 12);
    }

}