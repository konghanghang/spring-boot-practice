package com.ioc.annotation;

import com.ioc.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {

    /**
     * 共容器中注册一个bean，类型为返回值类型，名字为方法名
     * @return
     */
    @Bean
    public Person person(){
        return new Person("haha", 12);
    }

}
