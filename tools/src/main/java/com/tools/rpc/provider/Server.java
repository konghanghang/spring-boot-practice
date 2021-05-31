package com.tools.rpc.provider;

import com.tools.rpc.api.ServiceInterface;

import java.io.IOException;

/**
 * @author yslao@outlook.com
 * @since 2021/5/31
 */
public class Server {

    public static void main(String[] args) throws IOException {
        RegisterCenter registerCenter = new RegisterCenter(8888);
        //注册技师对象至注册中心
        registerCenter.register(ServiceInterface.class, ServiceInterfaceImpl.class);
        registerCenter.start();
    }

}
