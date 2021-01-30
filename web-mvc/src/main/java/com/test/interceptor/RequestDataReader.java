package com.test.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class RequestDataReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestDataReader.class);
    protected static final Set<HttpMethod> SUPPORTED_METHODS = EnumSet.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH);

    private ObjectMapper objectMapper;

    public RequestDataReader(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public boolean canRead(HttpInputMessage message) {
        MediaType mediaType = message.getHeaders().getContentType();
        if (!canRead(mediaType)) {
            return false;
        }
        HttpMethod httpMethod = (message instanceof HttpRequest ? ((HttpRequest) message).getMethod() : null);
        return canRead(httpMethod);
    }

    protected boolean canRead(MediaType mediaType) {
        if (mediaType == null){
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        LOGGER.info("Not support mediaType: {}", mediaType);
        return false;
    }

    protected boolean canRead(HttpMethod httpMethod) {
        if (httpMethod == null){
            return true;
        }
        if (SUPPORTED_METHODS.contains(httpMethod)) {
            return true;
        }
        LOGGER.debug("Not support request method: {}", httpMethod);
        return false;
    }

    public JsonNode read(HttpInputMessage message) throws IOException {
        InputStream inputStream = message.getBody();
        InputStream body;
        if (inputStream.markSupported()){
            inputStream.mark(1);
            body = (inputStream.read() != -1) ? inputStream : null;
        } else {
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
            int read = pushbackInputStream.read();
            if (read == -1){
                body = null;
            } else {
                body = pushbackInputStream;
                pushbackInputStream.unread(read);
            }
        }
        if (body != null) {
            return objectMapper.readTree(body);
        }
        return null;
    }

    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.APPLICATION_JSON);
    }

}
