package com.test;

import com.iminling.common.json.JsonUtil;
import com.iminling.core.annotation.EnableResolve;
import com.iminling.core.constant.ResolveStrategy;
import com.test.service.cache.CacheService;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "logging.file=target/test-logs/web-mvc")
@Import(WebMvcApplicationTest.ResolveController.class)
public class WebMvcApplicationTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private LettuceConnectionFactory redisConnectionFactory;

    @MockitoBean
    private GitProperties gitProperties;

    @Autowired
    private CacheService cacheService;

    @Test
    void preservesCacheReadUpdateAndEviction() {
        String original = cacheService.getId("migration");
        assertEquals(original, cacheService.getId("migration"));
        String updated = cacheService.update("migration");
        assertNotEquals(original, updated);
        assertEquals(updated, cacheService.getId("migration"));
        cacheService.deleteById("migration");
        assertNotEquals(updated, cacheService.getId("migration"));
    }

    @Test
    void startsApplicationAndServesExistingEndpoint() throws Exception {
        var response = HttpClient.newHttpClient().send(HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/index?name=lucy&age=10"))
                        .timeout(Duration.ofSeconds(10)).GET().build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("index", response.body());
    }

    @Test
    void loadsCoreFromJarAndResolvesParametersAndReturnValue() throws Exception {
        var response = HttpClient.newHttpClient().send(HttpRequest.newBuilder()
                        .uri(URI.create("http://127.0.0.1:" + port + "/migration/resolve"))
                        .timeout(Duration.ofSeconds(10)).header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"input\":{\"name\":\"lucy\"}}"))
                        .build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        var json = JsonUtil.getInstant().readTree(response.body());
        assertEquals(200, json.get("code").asInt());
        assertEquals("lucy", json.get("data").get("name").asString());
        assertEquals("9007199254740993", json.get("data").get("id").asString());
        assertTrue(json.get("data").get("id").isString());
    }

    @RestController
    static class ResolveController {
        @PostMapping("/migration/resolve")
        @EnableResolve(ResolveStrategy.ALL)
        public Output resolve(Input input) {
            return new Output(input.name(), 9007199254740993L);
        }
    }

    public record Input(String name) {}
    public record Output(String name, Long id) {}
}
