package com.test.config.rest;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;

public class EnhanceRestTemplate extends RestTemplate {

    private boolean enableTimeOut = true;

    public EnhanceRestTemplate() {}

    public EnhanceRestTemplate(boolean enableTimeOut) {
        this.enableTimeOut = enableTimeOut;
    }

    private AntPathMatcher pathMatcher = new AntPathMatcher();
    private ConcurrentMap<String, OkHttp3ClientHttpRequestFactory> factoryMap = new ConcurrentHashMap<>();
    {
        pathMatcher.setCaseSensitive(false);
    }

    @Override
    protected ClientHttpRequest createRequest(URI uri, HttpMethod method) throws IOException {
        if (!enableTimeOut) {
            return super.createRequest(uri, method);
        }
        Optional<UrlProperties> optional = findConfig(uri, method);
        if (!optional.isPresent()) {
            return super.createRequest(uri, method);
        }
        final UrlProperties urlProperties = optional.get();
        OkHttp3ClientHttpRequestFactory requestFactory = factoryMap.computeIfAbsent(
            urlProperties.getUrl(), key -> {
                OkHttpClient client = build(urlProperties).build();
                return new OkHttp3ClientHttpRequestFactory(client);
            });
        ClientHttpRequest request = requestFactory.createRequest(uri, method);
        return request;
    }

    protected Optional<UrlProperties> findConfig(URI uri, HttpMethod method) {
        String host = uri.getHost().toUpperCase() + ":" + uri.getPort();
        System.out.println("host:" + host);
        List<UrlProperties> urlPropertiesList = initUrl();
        if (urlPropertiesList == null || urlPropertiesList.isEmpty()) {
            return Optional.empty();
        }

        return urlPropertiesList.stream()
            .filter(u -> (Objects.isNull(u.getMethod()) || u.getMethod().equalsIgnoreCase(method.name()))
                && pathMatcher.match(u.getUrl(), uri.getPath())
            ).findFirst();
    }

    private List<UrlProperties> initUrl() {
        UrlProperties properties = new UrlProperties();
        properties.setUrl("/index");
        properties.setMethod("GET");
        properties.setConnectTimeout(1000);
        properties.setReadTimeout(1000);
        properties.setWriteTimeout(1000);
        return Lists.newArrayList(properties);
    }

    private OkHttpClient.Builder build(UrlProperties urlProperties) {
        return new OkHttpClient.Builder()
            .readTimeout(urlProperties.getReadTimeout(), TimeUnit.MILLISECONDS)
            .connectTimeout(urlProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)
            .writeTimeout(urlProperties.getWriteTimeout(), TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(100, 500, TimeUnit.SECONDS));
    }

}
