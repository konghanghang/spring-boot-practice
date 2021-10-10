package com.test.config.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iminling.common.json.JsonUtil;
import com.iminling.core.util.ThreadContext;
import com.iminling.model.common.ResultModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {
        log.info("===========================request begin================================================");
        log.info("URI         : {}", request.getURI());
        log.info("Method      : {}", request.getMethod());
        log.info("Headers     : {}", request.getHeaders());
        log.info("Request body: {}", new String(body, "UTF-8"));
        log.info("==========================request end================================================");
        ClientHttpResponse response = execution.execute(request, body);
        ClientHttpResponseWrapper clientHttpResponseWrapper = new ClientHttpResponseWrapper(response);
        log.info("============================response begin==========================================");
        log.info("Status code  : {}", clientHttpResponseWrapper.getStatusCode());
        log.info("Status text  : {}", clientHttpResponseWrapper.getStatusText());
        log.info("Headers      : {}", clientHttpResponseWrapper.getHeaders());
        log.info("=======================response end=================================================");
        clientHttpResponseWrapper.getHeaders().add("headerName", "VALUE");
        return clientHttpResponseWrapper;
    }

    static class ClientHttpResponseWrapper extends AbstractClientHttpResponse {

        private ClientHttpResponse response;
        private InputStream inputStream;

        ClientHttpResponseWrapper(ClientHttpResponse response) {
            this.response = response;
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.getStatusText();
        }

        @Override
        public void close() {
            if (this.inputStream != null) {
                try {
                    StreamUtils.drain(this.inputStream);
                    this.inputStream.close();
                } catch (Exception ignored) {
                }
            }
            this.response.close();
        }

        @Override
        public InputStream getBody() throws IOException {
            if (inputStream == null) {
                if (Boolean.valueOf(Optional.ofNullable(ThreadContext.getAttribute("resultModel")).map(Object::toString).orElse("true"))) {
                    inputStream = response.getBody();
                    return inputStream;
                }
                InputStream body = response.getBody();

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                StreamUtils.copy(body, os);

                ObjectMapper objectMapper = JsonUtil.getInstant();
                ResultModel<?> innerResponse = objectMapper.readValue(os.toByteArray(), ResultModel.class);
                inputStream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(innerResponse.getData()));
            }
            return inputStream;
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.response.getHeaders();
        }

    }
}
