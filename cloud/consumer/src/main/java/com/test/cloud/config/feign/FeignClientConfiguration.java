package com.test.cloud.config.feign;

import com.iminling.common.http.OkHttpUtils;
import com.iminling.core.config.feign.client.CustomizeFeignClient;
import com.test.cloud.config.feign.decode.ResponseDecoder;
import feign.Client;
import feign.codec.Decoder;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.LoadBalancerFeignRequestTransformer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author yslao@outlook.com
 * @since 2022/4/24
 */
// 只由 @EnableFeignClients.defaultConfiguration 导入到 Feign 子容器。
public class FeignClientConfiguration {

    private final ObjectProvider<FeignHttpMessageConverters> messageConverters;

    public FeignClientConfiguration(ObjectProvider<FeignHttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Bean
    @ConditionalOnMissingBean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new ResponseDecoder(new SpringDecoder(this.messageConverters)));
    }

    @Bean
    @ConditionalOnMissingBean(value = Client.class, search = SearchStrategy.CURRENT)
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                             LoadBalancerClientFactory loadBalancerClientFactory,
                             ObjectProvider<LoadBalancerFeignRequestTransformer> transformers) {
        OkHttpClient okHttpClient = OkHttpUtils.okHttpClientBuilder()
            .retryOnConnectionFailure(false).build();
        return new FeignBlockingLoadBalancerClient(new CustomizeFeignClient(okHttpClient),
                loadBalancerClient, loadBalancerClientFactory, transformers.orderedStream().toList());
    }

    // 使用与 MVC 相同的 core mapper，保留 Long、日期和 ResultModel 的 JSON 约定。
    @Bean
    public HttpMessageConverterCustomizer coreJsonMessageConverterCustomizer(JsonMapper mapper) {
        return converters -> converters.replaceAll(converter ->
                converter instanceof JacksonJsonHttpMessageConverter
                        ? new JacksonJsonHttpMessageConverter(mapper) : converter);
    }

}
