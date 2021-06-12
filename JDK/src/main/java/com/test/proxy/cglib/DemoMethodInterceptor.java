package com.test.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class DemoMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object rs = null;
        try {
            System.out.println("before cglib");
            rs = methodProxy.invokeSuper(o,objects);
        } finally {
            System.out.println("finally");
        }
        return rs;
    }
}
