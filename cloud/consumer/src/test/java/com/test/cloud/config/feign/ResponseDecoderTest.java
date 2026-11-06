package com.test.cloud.config.feign;

import com.iminling.common.json.JsonUtil;
import com.iminling.core.config.value.ResultModel;
import com.test.api.ProductInfo;
import feign.Request;
import feign.Response;
import feign.codec.Decoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.http.converter.autoconfigure.ClientHttpMessageConvertersCustomizer;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.core.ParameterizedTypeReference;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDecoderTest {
    private final Decoder decoder = decoder();

    private static Decoder decoder() {
        var beans = new DefaultListableBeanFactory();
        var configuration = new FeignClientConfiguration(beans.getBeanProvider(FeignHttpMessageConverters.class));
        beans.registerSingleton("coreJson", configuration.coreJsonMessageConverterCustomizer(JsonUtil.getInstant()));
        beans.registerSingleton("converters", new FeignHttpMessageConverters(
                beans.getBeanProvider(ClientHttpMessageConvertersCustomizer.class),
                beans.getBeanProvider(HttpMessageConverterCustomizer.class)));
        return configuration.feignDecoder();
    }

    @Test
    void unwrapsTypedDataUsingSpringCloudConverters() throws Exception {
        var type = new ParameterizedTypeReference<List<ProductInfo>>() {}.getType();
        var result = (List<?>) decoder.decode(response(
                "{\"code\":200,\"data\":[{\"productId\":17,\"productName\":\"Java 17\"}]}"), type);
        assertEquals(17, ((ProductInfo) result.get(0)).getProductId());
        assertEquals("Java 17", ((ProductInfo) result.get(0)).getProductName());
    }

    @Test
    void preservesExplicitResultModelAndStringResponses() throws Exception {
        var type = new ParameterizedTypeReference<ResultModel<ProductInfo>>() {}.getType();
        var result = (ResultModel<?>) decoder.decode(response(
                "{\"code\":200,\"data\":{\"productId\":17},\"message\":\"ok\"}"), type);
        assertEquals(17, ((ProductInfo) result.getData()).getProductId());
        assertEquals("ok", result.getMessage());
        assertEquals("hello", decoder.decode(response("{\"code\":200,\"data\":\"hello\"}"), String.class));
        assertNull(decoder.decode(response("{\"code\":200,\"data\":null}"), ProductInfo.class));
    }

    private static Response response(String body) {
        return Response.builder().status(200).reason("OK")
                .request(Request.create(Request.HttpMethod.GET, "http://localhost/products",
                        Map.of(), null, StandardCharsets.UTF_8, null))
                .headers(Map.of("Content-Type", List.of("application/json")))
                .body(body, StandardCharsets.UTF_8).build();
    }
}
