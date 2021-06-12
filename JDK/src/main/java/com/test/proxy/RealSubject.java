package com.test.proxy;

/**
 * @author yslao@outlook.com
 * @since 2021/6/12
 */
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("======request======");
    }

    @Override
    public void hello() {
        System.out.println("========hello======");
    }
}
