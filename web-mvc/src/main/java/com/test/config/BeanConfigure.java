package com.test.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.interceptor.GlobalInterceptor;
import com.test.interceptor.RequestDataReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Configuration
public class BeanConfigure {

    @Bean
    @ConditionalOnMissingBean(name = "requestDataReader")
    public RequestDataReader getRequestDataReader() {
        return new RequestDataReader(getObjectMapper());
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 设置日期对象的输出格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINESE));
        // 设置输入时忽略在json字符串中存在 但在java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Bean
    @ConditionalOnMissingBean(name = "globalInterceptor")
    public GlobalInterceptor getGlobalInterceptor() {
        return new GlobalInterceptor(getRequestDataReader());
    }


}
