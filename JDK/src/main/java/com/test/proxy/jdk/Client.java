package com.test.proxy.jdk;

import com.test.proxy.RealSubject;
import com.test.proxy.Subject;

import java.lang.reflect.Proxy;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class Client {

    public static void main(String[] args) {
        Subject subject = (Subject) Proxy.newProxyInstance(Client.class.getClassLoader(),new Class[]{Subject.class},new JdkProxySubject(new RealSubject()));
        subject.request();
        subject.hello();
    }

}
