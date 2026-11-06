package com.test.cloud;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.gateway.config.GatewayProperties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.cloud.nacos.discovery.enabled=false",
        "spring.cloud.nacos.config.enabled=false"
})
class GatewayApplicationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private GatewayProperties gatewayProperties;

    @Test
    void bindsMigratedRoutesAndRunsGlobalFilter() throws Exception {
        assertEquals(URI.create("lb://provider-dept"), gatewayProperties.getRoutes().get(0).getUri());
        var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        var request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/dept/list"))
                .timeout(Duration.ofSeconds(10)).GET().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }
}
