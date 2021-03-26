package com.test.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 请求拦截器，在每个请求都添加token请求头
 * @author yslao@outlook.com
 * @since 2021/3/26
 */
public class TokenInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("token", "aaaa");
    }

}
