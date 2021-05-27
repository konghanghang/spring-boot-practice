package com.test.cloud.config.feign;

import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
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

    @Bean
    public Contract contract() {
        return new CustomizeContract(invokeContent);
    }

    @Bean
    public Decoder customizeDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new OptionalDecoder(new ResponseEntityDecoder(new CustomizeDecoder(messageConverters)));
    }

    /**
     * Feign.Builder
     * @return
     */
    @Bean
    @Scope("prototype")
    public Feign.Builder builder(Contract contract, Decoder customizeDecoder) {
        Feign.Builder builder = Feign.builder();
        builder.requestInterceptor(new TokenInterceptor())
                .contract(contract)
                .decoder(customizeDecoder)
                .invocationHandlerFactory(new CustomizeInvocationHandlerFactory(applicationContext, invokeContent));
        return builder;
    }
}
