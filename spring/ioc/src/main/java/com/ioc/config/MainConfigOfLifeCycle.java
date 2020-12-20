package com.ioc.config;

import com.ioc.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * bean的生命周期
 *      bean创建->初始化->销毁
 *
 * BeanPostProcessor.postProcessBeforeInitialization
 * 初始化：
 *      对象创建完成，并赋值好，调用初始化方法
 * BeanPostProcessor.postProcessAfterInitialization
 * 销毁：
 *      单实例bean，容器关闭时销毁
 *      多实例bean，容器不会管理bean
 *
 * 1. 指定初始化和销毁方法
 *      通过@Bean指定initMethod和destroyMethod方法
 * 2. 通过让bean实现InitializingBean(定义初始化逻辑)
 *                DisposableBean(定义销毁逻辑)
 * 3. 可以使用jsr250
 *      @PostConstruct: 在bean创建完成，并赋值好，来执行初始化方法
 *      @PreDestroy： 在容器销毁bean之前通知我们进行清理工作
 * 4. BeanPostProcessor[interface]: bean的后置处理
 *      在bean的初始化前后进行一些处理工作：
 *      postProcessBeforeInitialization：在初始化之前工作
 *      postProcessAfterInitialization： 在初始化之后工作
 *
 */
@ComponentScan("com.ioc.bean")
@Configuration
public class MainConfigOfLifeCycle {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public Car car(){
        return new Car();
    }

}
