package com.test.cloud.config.feign.client;

import feign.Client;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Request.Options;
import feign.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.*;

/**
 * @author yslao@outlook.com
 * @since 2022/4/24
 */
public class CustomizeFeignClient implements Client {

    private final OkHttpClient delegate;

    public CustomizeFeignClient(OkHttpClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request, Options options) throws IOException {
        OkHttpClient requestClient;
        if (delegate.connectTimeoutMillis() != options.connectTimeoutMillis()
            || delegate.readTimeoutMillis() != options.readTimeoutMillis()) {
            requestClient = delegate.newBuilder()
                .connectTimeout(options.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(options.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                .build();
        } else {
            requestClient = delegate;
        }
        okhttp3.Request newRequest = toOkHttpRequest(request);
        okhttp3.Response response = requestClient.newCall(newRequest).execute();
        return toFeignResponse(response, request);
    }

    private static okhttp3.Request toOkHttpRequest(Request request) {
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        requestBuilder.url(request.url());

        MediaType mediaType = null;
        boolean hasAcceptHeader = false;
        for (String field : request.headers().keySet()) {
            if ("Accept".equalsIgnoreCase(field)) {
                hasAcceptHeader = true;
            }
            for (String value : request.headers().get(field)) {
                if ("Content-Type".equalsIgnoreCase(field)) {
                    mediaType = MediaType.parse(value);
                    if (request.charset() != null && mediaType != null) {
                        mediaType.charset(request.charset());
                    }
                } else {
                    requestBuilder.addHeader(field, value);
                }
            }
        }
        if (!hasAcceptHeader) {
            requestBuilder.addHeader("Accept", "*/*");
        }
        byte[] inputBody = request.requestTemplate().body();
        boolean isMethodWithBody = HttpMethod.POST.equals(request.httpMethod())
            || HttpMethod.PUT.equals(request.httpMethod());
        if (isMethodWithBody && inputBody == null) {
            // write an empty BODY to conform with okhttp 2.4.0+
            // http://johnfeng.github.io/blog/2015/06/30/okhttp-updates-post-wouldnt-be-allowed-to-have-null-body/
            inputBody = new byte[0];
        }
        RequestBody body = inputBody != null ? RequestBody.create(mediaType, inputBody) : null;
        requestBuilder.method(request.httpMethod().name(), body);
        return requestBuilder.build();
    }

    private static feign.Response toFeignResponse(okhttp3.Response response, feign.Request request) throws IOException {
        return feign.Response.builder()
            .status(response.code())
            .reason(response.message())
            .headers(toMap(response.headers()))
            .body(toBody(response.body()))
            .request(request)
            .build();
    }

    private static Map<String, Collection<String>> toMap(Headers headers) {
        return (Map) headers.toMultimap();
    }

    private static feign.Response.Body toBody(final ResponseBody responseBody) throws IOException {
        if (responseBody == null || responseBody.contentLength() == 0) {
            if (responseBody != null) {
                responseBody.close();
            }
            return null;
        }
        final Integer length = responseBody.contentLength() >= 0 && responseBody.contentLength() <= Integer.MAX_VALUE ?
            (int) responseBody.contentLength() : null;

        return new feign.Response.Body() {
            @Override
            public void close() throws IOException {
                responseBody.close();
            }

            @Override
            public Integer length() {
                return length;
            }

            @Override
            public boolean isRepeatable() {
                return false;
            }

            @Override
            public InputStream asInputStream() throws IOException {
                return responseBody.byteStream();
            }

            @Override
            public Reader asReader(Charset charset) throws IOException {
                return responseBody.charStream();
            }
        };
    }
}
