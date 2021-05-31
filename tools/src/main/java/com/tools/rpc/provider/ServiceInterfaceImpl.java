package com.tools.rpc.provider;

import com.tools.rpc.api.ServiceInterface;

/**
 * @author yslao@outlook.com
 * @since 2021/5/31
 */
public class ServiceInterfaceImpl implements ServiceInterface {
    @Override
    public String call(String name) {
        return "hello:" + name;
    }
}
