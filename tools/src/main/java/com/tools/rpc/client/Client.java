package com.tools.rpc.client;

import com.tools.rpc.api.ServiceInterface;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * @author yslao@outlook.com
 * @since 2021/5/31
 */
public class Client {

    public static void main(String[] args) throws Exception {
        ServiceInterface remoteProxyObj = getRemoteProxyObj(ServiceInterface.class);
        System.out.println(remoteProxyObj.call("zhangsan"));
    }

    public static <T> T getRemoteProxyObj(final Class<?> serviceInterface)
            throws Exception {
        // 默认端口8888
        InetSocketAddress serviceAddr = new InetSocketAddress("127.0.0.1",8888);
        // 1.将本地的接口调用转换成JDK的动态代理，在动态代理中实现接口的远程调用
        //进行实际的服务调用(动态代理)
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new RpcClientInvocationHandler(serviceInterface, serviceAddr));
    }

}
