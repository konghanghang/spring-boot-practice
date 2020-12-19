package com.ioc.annotation;

import com.ioc.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// 包扫描，排除规则
/*@ComponentScan(value = "com.ioc", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})
})*/
// 只包含什么注解， 要禁用默认的filter
@ComponentScan(value = "com.ioc", includeFilters = {
        // 按照注解类型
        // @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class}),
        // 按照给定类型
        // @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {BookService.class}),
        // 自定义扫描规则
        @ComponentScan.Filter(type = FilterType.CUSTOM, value = MyTypeFilter.class)
}, useDefaultFilters = false)
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
