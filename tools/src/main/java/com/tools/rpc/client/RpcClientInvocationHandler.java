package com.tools.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author yslao@outlook.com
 * @since 2021/5/31
 */
public class RpcClientInvocationHandler implements InvocationHandler {

    //远程调用的服务
    private Class serviceClass;
    //远程调用地址
    private final InetSocketAddress addr;

    public RpcClientInvocationHandler(Class serviceClass,InetSocketAddress addr) {
        this.serviceClass = serviceClass;
        this.addr = addr;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(addr);
            //类名 方法名 方法类型列表  方法入参列表
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeUTF(serviceClass.getSimpleName());
            outputStream.writeUTF(method.getName());
            outputStream.writeObject(method.getParameterTypes());
            outputStream.writeObject(args);
            outputStream.flush();

            inputStream = new ObjectInputStream(socket.getInputStream());
            //我们要把调用的细节打印出来
            System.out.println("远程调用成功！" + serviceClass.getName());
            //最后要网络的请求返回给返回
            return inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
            inputStream.close();
            outputStream.close();
        }
        return null;
    }
}
