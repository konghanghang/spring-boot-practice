package com.ioc.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Car implements InitializingBean, DisposableBean {

    @PostConstruct
    public void postConstruct(){
        System.out.println("PostConstruct.....");
    }

    /**
     * 容器移除对象之前
     */
    @PreDestroy
    public void preDestroy(){
        System.out.println("PreDestroy....");
    }

    public Car(){
        System.out.println("car constructor...");
    }

    public void init(){
        System.out.println("car init....");
    }

    public void myDestroy(){
        System.out.println("car myDestroy...");
    }

    @Override
    public void destroy(){
        System.out.println("car DisposableBean destroy...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }


}
