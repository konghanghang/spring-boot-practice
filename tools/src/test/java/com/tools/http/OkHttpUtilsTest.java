package com.tools.http;

import java.io.IOException;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OkHttpUtilsTest {

    private OkHttpClient client;

    @BeforeEach
    void beforeAll() {
        client = OkHttpUtils.okHttpClientBuilder().build();
    }

    @Test
    void testGetCall() throws IOException {
        Request request = new Request.Builder()
            // .url("http://192.168.31.173:2007/shortUrl/remain/import/amount")
            .url("http://192.168.31.173:2007/shortUrl/task/1")
            .get()
            .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    @Test
    void testPostCall() throws IOException {
        String body = "{\"name\":null,\"status\":null,\"startTime\":null,\"endTime\":null,\"del\":null,\"enable\":null,\"companyName\":\"\",\"sortField\":\"createTime\",\"sortOrder\":\"descending\",\"aiTaskId\":\"\",\"pageNo\":1,\"pageSize\":10,\"currentNo\":1,\"sort\":\"\",\"go\":0}";
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=UTF-8"));
        Request request = new Request.Builder()
            .url("http://192.168.31.173:2007/shortUrl/tasks/admin")
            .post(requestBody)
            .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

}