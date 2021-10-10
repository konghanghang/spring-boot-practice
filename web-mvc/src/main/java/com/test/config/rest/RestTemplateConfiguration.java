package com.test.config.rest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    /**
     * RestTemplate包含以下几个部分：
     * HttpMessageConverter 对象转换器
     * ClientHttpRequestFactory 默认是JDK的HttpURLConnection
     * ResponseErrorHandler 异常处理
     * ClientHttpRequestInterceptor 请求拦截器
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        OkHttpClient okHttpClient = new Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(50, 100, TimeUnit.SECONDS)).build();
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        RestTemplate restTemplate = new EnhanceRestTemplate();
        restTemplate.setRequestFactory(okHttp3ClientHttpRequestFactory);
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new RestTemplateLoggingInterceptor());
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

}
