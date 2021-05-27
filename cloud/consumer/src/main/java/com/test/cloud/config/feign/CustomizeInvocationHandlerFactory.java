package com.test.cloud.config.feign;

import feign.InvocationHandlerFactory;
import feign.Target;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 自定义feign调用处理工厂
 * @author yslao@outlook.com
 * @since 2021/3/26
 */
public class CustomizeInvocationHandlerFactory implements InvocationHandlerFactory {

    private final ApplicationContext applicationContext;
    private final InvokeContent invokeContent;

    public CustomizeInvocationHandlerFactory(ApplicationContext applicationContext,
                                             InvokeContent invokeContent) {
        this.applicationContext = applicationContext;
        this.invokeContent = invokeContent;
    }

    @Override
    public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
        return new CustomizeInvocationHandler(applicationContext, target, dispatch, invokeContent);
    }
}
