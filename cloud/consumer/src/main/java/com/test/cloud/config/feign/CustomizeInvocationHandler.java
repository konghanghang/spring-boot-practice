package com.test.cloud.config.feign;

import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * 自定义feign调用处理
 * @author yslao@outlook.com
 * @since 2021/3/26
 */
public class CustomizeInvocationHandler implements InvocationHandler {

    private final ApplicationContext applicationContext;
    private final Target target;
    private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;
    private final InvokeContent invokeContent;

    public CustomizeInvocationHandler(ApplicationContext applicationContext, Target target,
                                      Map<Method, InvocationHandlerFactory.MethodHandler> dispatch,
                                      InvokeContent invokeContent) {
        this.applicationContext = applicationContext;
        this.target = target;
        this.dispatch = dispatch;
        this.invokeContent = invokeContent;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler =
                        args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return hashCode();
        } else if ("toString".equals(method.getName())) {
            return toString();
        }
        // 改变参数
        args = resolveArgs(method, args);
        return dispatch.get(method).invoke(args);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomizeInvocationHandler) {
            CustomizeInvocationHandler other = (CustomizeInvocationHandler) obj;
            return target.equals(other.target);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public String toString() {
        return target.toString();
    }

    /**
     * 处理参数
     * @param method
     * @param args
     * @return
     */
    private Object[] resolveArgs(Method method, Object[] args) {
        String configKey = Feign.configKey(target.type(), method);
        String[] parameterNames = invokeContent.getParameterNames(configKey);
        if (Objects.nonNull(args)) {
            List<Object> newArgList = new ArrayList<>(args.length);
            Map<String, Object> map = new HashMap<>();
            newArgList.add(map);
            for (int i = 0; i < args.length; i++) {
                if (Objects.nonNull(parameterNames[i])) {
                    map.put(parameterNames[i], args[i]);
                } else {
                    newArgList.add(args[i]);
                }
            }
            args = newArgList.toArray();
        }
        return args;
    }
}
