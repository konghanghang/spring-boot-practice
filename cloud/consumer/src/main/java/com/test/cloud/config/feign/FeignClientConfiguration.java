package com.test.cloud.config.feign;

import com.iminling.common.http.OkHttpUtils;
import com.test.cloud.config.feign.decode.ResponseDecoder;
import feign.Client;
import feign.codec.Decoder;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author yslao@outlook.com
 * @since 2022/4/24
 */
@Configuration
public class FeignClientConfiguration {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public FeignClientConfiguration(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    @ConditionalOnMissingBean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new ResponseDecoder(new SpringDecoder(this.messageConverters)));
    }

    @Bean
    @ConditionalOnMissingBean
    public Client feignClient() {
        OkHttpClient okHttpClient = OkHttpUtils.Companion.okHttpClientBuilder()
            .retryOnConnectionFailure(false).build();
        // CustomizeFeignClient feignOkHttpClient = new CustomizeFeignClient(okHttpClient);
        return new feign.okhttp.OkHttpClient(okHttpClient);
        // return new LoadBalancerFeignClient(feignOkHttpClient, cachingFactory, clientFactory);
    }

    /**
     * 处理：@FeignClient中的@RequestMapping也被SpringMVC加载的问题解决
     * @return
     */
    @Bean
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignRequestMappingHandlerMapping();
            }
        };
    }

    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return super.isHandler(beanType) &&
                !AnnotatedElementUtils.isAnnotated(beanType, FeignClient.class);
        }
    }

}
