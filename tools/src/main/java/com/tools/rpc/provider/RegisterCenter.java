package com.tools.rpc.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yslao@outlook.com
 * @since 2021/5/31
 */
public class RegisterCenter {

    //线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //定义注册中心的静态对象
    private static Map<String, Class> serviceRegistry = new HashMap<>();
    //服务端口
    private static int port = 8888;

    /**
     *  注册服务
     * @param serviceInterface 接口名字
     * @param impl 实现类的class对象
     */
    public void register(Class serviceInterface, Class impl) {
        //服务的注册:socket通讯+反射
        serviceRegistry.put(serviceInterface.getSimpleName(), impl);
    }

    public RegisterCenter(int port) {
        this.port = port;
    }


    /**
     * 启动服务端
     * @throws IOException
     */
    public static void start() throws IOException {
        // 创建ServerSocket实例监听端口
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("start server");
        // 1.监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行,并且同时将socket送入(server.accept()=socket)
        try {
            while (true) {
                //serverSocket.accept()会阻塞直到服务端接受到客户端的请求。
                System.out.println("----------server11---");
                executorService.execute(new ServiceTask(serverSocket.accept()));
                System.out.println("----------server22---");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将客户端的每一个请求都封装成一个线程ServiceTask，投放到线程池里面进行执行。
     * @author hasee
     *
     */
    private static class ServiceTask implements Runnable {
        private Socket client;
        public ServiceTask(Socket client) {
            this.client = client;
        }
        public void run() {
            //读取socket中的流数据
            ObjectInputStream inputStream = null;
            ObjectOutputStream outputStream = null;
            try {
                // 类名、方法名、参数类型、参数值
                inputStream = new ObjectInputStream(client.getInputStream());
                //获取调用服务名称
                String serviceName = inputStream.readUTF();
                //获取调用方法的名称
                String methodName = inputStream.readUTF();
                //获取参数类型列表
                Class<?>[] requiresTypes = (Class<?>[]) inputStream.readObject();
                //获取参数列表
                Object[] args = (Object[]) inputStream.readObject();
                Class serviceClass = serviceRegistry.get(serviceName);
                //反射调用方法
                Method method = serviceClass.getMethod(methodName, requiresTypes);
                Object result = method.invoke(serviceClass.newInstance(), args);
                //把结果反馈到客户端
                outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.writeObject(result);
                outputStream.flush();
                //关闭io资源
                inputStream.close();
                client.close();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
