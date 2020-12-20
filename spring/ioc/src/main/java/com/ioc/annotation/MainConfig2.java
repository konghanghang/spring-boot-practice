package com.ioc.annotation;

import com.ioc.Person;
import com.ioc.condition.LinuxCondition;
import com.ioc.condition.MacCondition;
import com.ioc.condition.WindowsCondition;
import org.springframework.context.annotation.*;

/**
 * 如果condition放在类上，则满足条件类中的bean都会被注册 ，如果不满足条件，则都不会注册
 * 类和方法上都标注，则以方法上的为主
 */
@Conditional(MacCondition.class)
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

}
