package com.test.proxy.cglib;

import com.test.proxy.RealSubject;
import com.test.proxy.Subject;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class Client {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealSubject.class);
        enhancer.setCallback(new DemoMethodInterceptor());
        Subject subject = (Subject) enhancer.create();
        subject.hello();
    }

}
