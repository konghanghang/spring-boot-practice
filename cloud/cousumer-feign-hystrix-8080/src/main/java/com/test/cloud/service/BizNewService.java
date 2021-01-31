package com.test.cloud.service;

import org.springframework.stereotype.Component;

/**
 * 用来处理服务宕机时，返回的异常信息
 */
@Component
public class BizNewService implements IBizService{
    @Override
    public String ok(String id) {
        return "bizNewService fallback ok, 出现异常";
    }

    @Override
    public String time(String id) {
        return "bizNewService fallback time, 出现异常";
    }
}
