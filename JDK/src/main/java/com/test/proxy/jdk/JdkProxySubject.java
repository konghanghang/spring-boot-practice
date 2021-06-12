package com.test.proxy.jdk;

import com.test.proxy.RealSubject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class JdkProxySubject implements InvocationHandler {

    private RealSubject realSubject;

    public JdkProxySubject(RealSubject realSubject){
        this.realSubject = realSubject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object rs = null;
        try {
            rs = method.invoke(realSubject,args);
        } finally {
            System.out.println("finally");
        }
        return rs;
    }
}
