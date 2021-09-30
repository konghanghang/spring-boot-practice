package com.tools.http;

import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class OkHttpUtils {

    public static OkHttpClient.Builder okHttpClientBuilder() {

        return new OkHttpClient.Builder()
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .writeTimeout(500, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(500, 1000, TimeUnit.SECONDS))
            .addInterceptor(new OkHttpLoggingInterceptor());
    }

}
