package com.test.config.rest;

import com.iminling.core.util.ThreadContext;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class RestTemplateLoggingInterceptorTest {

    private static final String BODY = "{\"code\":200,\"data\":{\"name\":\"测试\"},\"message\":\"ok\"}";

    @AfterEach
    void clearContext() {
        ThreadContext.clear();
    }

    @Test
    void keepsResultEnvelopeByDefault() throws Exception {
        var upstream = new MockClientHttpResponse(BODY.getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
        try (var response = new RestTemplateLoggingInterceptor().intercept(
                new MockClientHttpRequest(HttpMethod.GET, URI.create("http://localhost/example")),
                new byte[0], (request, body) -> upstream)) {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("VALUE", response.getHeaders().getFirst("headerName"));
            assertEquals(BODY, new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    @Test
    void unwrapsDataOnlyWhenRequested() throws Exception {
        ThreadContext.setAttribute("resultModel", false);
        var upstream = new MockClientHttpResponse(BODY.getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
        try (var response = new RestTemplateLoggingInterceptor.ClientHttpResponseWrapper(upstream)) {
            assertEquals("{\"name\":\"测试\"}", new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    @Test
    void preservesCustomErrorBodyOnSpring7ErrorHandler() {
        var upstream = new MockClientHttpResponse("失败原因".getBytes(StandardCharsets.UTF_8), HttpStatus.BAD_REQUEST);
        upstream.getHeaders().setContentType(new org.springframework.http.MediaType("text", "plain", StandardCharsets.UTF_8));
        var error = assertThrows(RuntimeException.class, () -> new RestTemplateErrorHandler()
                .handleError(URI.create("http://localhost/example"), HttpMethod.GET, upstream));
        assertEquals("失败原因", error.getMessage());
    }
}
