package com.tools.http;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import okhttp3.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OkHttpUtilsTest {

    private OkHttpClient client;
    private Response response;

    @BeforeEach
    void beforeAll() {
        client = OkHttpUtils.okHttpClientBuilder().build();
    }

    @AfterEach
    void afterEach() {
        if (Objects.nonNull(response)) {
            response.body().close();
        }
    }

    @Test
    void testGetCall() throws IOException {
        Request request = new Request.Builder()
            // .url("http://192.168.31.173:2007/shortUrl/remain/import/amount")
            .url("http://192.168.31.173:2007/shortUrl/task/1")
            .get()
            .build();
        response = client.newCall(request).execute();
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
        response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    @Test
    void testPostFormCall() throws IOException {
        RequestBody requestBody = new FormBody.Builder()
            .add("title", "123")
            .add("message", "hi")
            .add("code", "10000")
            .build();
        Request request = new Request.Builder()
            .url("http://127.0.0.1:5001/test/form")
            .post(requestBody)
            .build();
        response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    @Test
    void testFile() throws IOException {
        RequestBody requestBody = RequestBody.create(new File("D:\\1.json"), MediaType.parse("multipart/form-data"));
        MultipartBody multipartBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "2.json", requestBody)
            .addFormDataPart("name", "小明")
            .build();
        Request request = new Request.Builder()
            .url("http://127.0.0.1:5001/test/file")
            .post(multipartBody)
            .build();
        response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

}