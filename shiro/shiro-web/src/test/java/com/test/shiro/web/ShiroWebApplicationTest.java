package com.test.shiro.web;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShiroWebApplicationTest {

    @LocalServerPort
    private int port;

    @Test
    void missingTokenIsRejectedByJakartaFilter() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/v1/resource")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.body()).isEqualTo("\"USER_UNAUTHORIZED\"");
    }

    @Test
    void anonymousPathPassesThroughToMvc() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/login")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        // 示例没有登录 Controller；404 表示已通过 anon 链并到达 MVC。
        assertThat(response.statusCode()).isEqualTo(404);
    }
}
