package com.test.feign;

import feign.Feign;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * feign配置类
 * @author yslao@outlook.com
 * @since 2021/3/26
 */
public class FeignConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private InvokeContent invokeContent;

    public FeignConfiguration(InvokeContent invokeContent) {
        this.invokeContent = invokeContent;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Feign.Builder
     * @return
     */
    @Bean
    @Scope("prototype")
    public Feign.Builder builder() {
        Feign.Builder builder = Feign.builder();
        builder.requestInterceptor(new TokenInterceptor())
                .invocationHandlerFactory(new CustomizeInvocationHandlerFactory(applicationContext, invokeContent));
        return builder;
    }
}
