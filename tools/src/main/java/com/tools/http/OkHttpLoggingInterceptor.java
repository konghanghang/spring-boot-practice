package com.tools.http;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class OkHttpLoggingInterceptor implements Interceptor {

    private static Set<String> ignoreHeaders = Sets
        .newHashSet("User-Agent", "Cookie", "Accept", "Sec-Fetch-Dest", "Accept-Language",
            "Cache-Control", "Sec-Fetch-Mode", "Connection", "Accept-Encoding", "Upgrade-Insecure-Requests",
            "Sec-Fetch-Site", "Sec-Fetch-User");

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        String url = request.url().toString();
        String method = request.method();
        String body = null;
        if (request.body() != null) {
            try {
                final okhttp3.Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                body = buffer.readUtf8();
            } catch (final IOException e) {
                log.error("", e);
            }
        }
        Map<String, List<String>> stringListMap = request.headers().toMultimap();
        stringListMap.entrySet().removeIf(entry -> ignoreHeaders.contains(entry.getKey()));
        log.info("url:{}, method:{}, body:{}, headers:{}", url, method, body, stringListMap);
        Exception error = null;
        Response response = null;
        try {
            response = chain.proceed(request);
            return response;
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            String responseBody = buffer.clone().readUtf8();
            log.info("response:{}", responseBody);
        }
    }
}
