package com.test.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CglibCompatibilityTest {
    public static class Greeting {
        public String greet(String name) {
            return "Hello " + name;
        }
    }

    @Test
    void createsAndInvokesProxyOnJava17WithoutAddOpens() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Greeting.class);
        enhancer.setCallback((MethodInterceptor) (object, method, args, proxy) ->
                proxy.invokeSuper(object, args) + "!");
        Greeting greeting = (Greeting) enhancer.create();
        assertEquals("Hello Java 17!", greeting.greet("Java 17"));
    }
}
