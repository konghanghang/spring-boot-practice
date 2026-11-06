package com.tools;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ToolsApplicationTest {
    @LocalServerPort
    private int port;

    @Test
    void servesCaptchaWithSessionCookie() throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/genCaptcha"))
                .GET().build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("content-type").orElseThrow().startsWith("image/jpeg"));
        assertTrue(response.headers().firstValue("set-cookie").orElseThrow().startsWith("pic_name_final="));
        var image = ImageIO.read(new ByteArrayInputStream(response.body()));
        assertNotNull(image);
        assertEquals(108, image.getWidth());
        assertEquals(40, image.getHeight());
    }
}
