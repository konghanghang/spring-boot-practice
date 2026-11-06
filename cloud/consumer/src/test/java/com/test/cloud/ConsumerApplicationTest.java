package com.test.cloud;

import com.sun.net.httpserver.HttpServer;
import com.test.api.ProductInfo;
import com.test.cloud.service.ConsumerProductInfoService;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.cloud.nacos.discovery.enabled=false",
        "spring.cloud.nacos.config.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "logging.file.name=target/consumer-test"
})
class ConsumerApplicationTest {
    private static final HttpServer PROVIDER = provider();

    @Autowired
    private ConsumerProductInfoService service;

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void localProvider(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.openfeign.client.config.productInfo.url",
                () -> "http://127.0.0.1:" + PROVIDER.getAddress().getPort());
        registry.add("spring.cloud.openfeign.client.config.indexClient.url",
                () -> "http://127.0.0.1:" + PROVIDER.getAddress().getPort());
    }

    @AfterAll
    static void stopProvider() {
        PROVIDER.stop(0);
    }

    @Test
    void realFeignClientsDecodeCoreResponseEnvelopes() throws Exception {
        var product = new ProductInfo();
        product.setProductName("Java 17");
        assertEquals(17, service.add(product));
        var found = service.get(17);
        assertEquals(17, found.getProductId());
        assertEquals("Java 17", found.getProductName());
        var request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/product/17"))
                .timeout(Duration.ofSeconds(10)).GET().build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Java 17"));
    }

    private static HttpServer provider() {
        try {
            var server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            server.createContext("/product", exchange -> {
                try (exchange) {
                    exchange.getRequestBody().readAllBytes();
                    String data = exchange.getRequestURI().getPath().endsWith("/add")
                            ? "17" : "{\"productId\":17,\"productName\":\"Java 17\"}";
                    byte[] body = ("{\"code\":200,\"data\":" + data + ",\"message\":\"ok\"}")
                            .getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, body.length);
                    exchange.getResponseBody().write(body);
                }
            });
            server.start();
            return server;
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
