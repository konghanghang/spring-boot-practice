package com.test.provider;

import com.iminling.common.json.JsonUtil;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.cloud.nacos.discovery.enabled=false",
        "spring.cloud.nacos.config.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "logging.file.name=target/provider-test"
})
class ProviderApplicationTest {
    @LocalServerPort
    private int port;

    @Test
    void productEndpointsRetainCoreResponseWrapping() throws Exception {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        HttpRequest add = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/product/add"))
                .timeout(Duration.ofSeconds(10)).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"productName\":\"Java 17\",\"productPrice\":\"19.00\"}"))
                .build();
        var added = client.send(add, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, added.statusCode());
        var json = JsonUtil.getInstant().readTree(added.body());
        assertEquals(200, json.path("code").asInt());
        int id = json.path("data").asInt();

        var get = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/product/" + id))
                .timeout(Duration.ofSeconds(10)).GET().build();
        var found = client.send(get, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, found.statusCode());
        var data = JsonUtil.getInstant().readTree(found.body()).path("data");
        assertEquals(id, data.path("productId").asInt());
        assertEquals("Java 17", data.path("productName").asString());
        assertEquals("19.00", data.path("productPrice").asString());
    }
}
