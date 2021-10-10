package com.test.config.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Charset charset = getCharset(response);
        byte[] responseBody = getResponseBody(response);
        String responseText = null;
        if (charset != null && responseBody.length > 0) {
            responseText = new String(responseBody, charset).trim();
        }
        throw new RuntimeException(responseText);
    }

    @Override
    protected Charset getCharset(ClientHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        MediaType contentType = headers.getContentType();
        return (contentType != null && contentType.getCharset() != null) ? contentType.getCharset() : Charset.forName(
            "UTF-8");
    }

}
