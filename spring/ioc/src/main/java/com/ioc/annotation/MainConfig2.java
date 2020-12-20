package com.ioc.annotation;

import com.ioc.Person;
import com.ioc.bean.Color;
import com.ioc.bean.ColorFactoryBean;
import com.ioc.condition.*;
import org.springframework.context.annotation.*;

/**
 * 给容器中注册组件
 * 1. 包扫描+组件标注注解（@Controller）
 * 2. @Bean 导入第三方包里边的组件
 * 3. @Import 快速给容器中导入一个组件
 *      1. @Import(要导入到容器中的组件) id为全类名
 *      2. ImportSelector 返回需要导入的组件的全类名数组
 *      3. ImportBeanDefinitionRegistrar 手动注册bean到容器中
 * 4. 使用spring提供的FactoryBean(工厂Bean)
 *      1. 默认获取到的是工厂bean调用getObject创建的对象
 *      2. 要获取工厂bean本身，需要给id前面加一个& @see org.springframework.beans.factory.BeanFactory
 * 如果condition放在类上，则满足条件类中的bean都会被注册 ，如果不满足条件，则都不会注册
 * 类和方法上都标注，则以方法上的为主
 */
@Import({Color.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
//@Conditional(MacCondition.class)
@Configuration
public class MainConfig2 {

    /**
     * singleton 单实例（默认值） ioc容器启动会去调用方法创建对象放到ioc容器中，以后每次使用都去ioc容器中获取
     * prototype 多实例         ioc容器启动不会去调用方法创建对象，每次使用时才会调用方法获取对象，每次都是一个新对象
     *
     * @return
     * @Lazy 容器创建的时候不创建对象，在第一次使用时创建对象
     */
    @Lazy
    @Scope("singleton")
    @Bean
    public Person person() {
        System.out.println("向ioc中添加bean。。。。");
        return new Person("haha", 12);
    }

    @Conditional(WindowsCondition.class)
    @Bean("bill")
    public Person person01() {
        return new Person("bill", 12);
    }

    @Conditional(LinuxCondition.class)
    @Bean("linus")
    public Person person02() {
        return new Person("linus", 12);
    }

    @Conditional(MacCondition.class)
    @Bean("jobs")
    public Person person03() {
        return new Person("jobs", 12);
    }

    @Bean
    public ColorFactoryBean colorFactoryBean(){
        return new ColorFactoryBean();
    }

}
