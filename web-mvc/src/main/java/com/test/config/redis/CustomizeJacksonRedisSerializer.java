package com.test.config.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class CustomizeJacksonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final JavaType javaType;

    private ObjectMapper objectMapper = new ObjectMapper();

    public CustomizeJacksonRedisSerializer(Class<T> type) {
        this.javaType = getJavaType(type);
    }

    public CustomizeJacksonRedisSerializer(JavaType type) {
        this.javaType = type;
    }

    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String originStr = new String(bytes);
        if (originStr.startsWith("{") && originStr.endsWith("}")) {
            try {
                this.objectMapper.readValue(originStr, javaType);
                return (T) this.objectMapper.readValue(bytes, 0, bytes.length, javaType);
            } catch (Exception ex) {
                throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
            }
        } else {
            return (T)originStr;
        }
    }

    @Override
    public byte[] serialize(@Nullable Object t) throws SerializationException {

        if (t == null) {
            return new byte[0];
        }
        try {
            if (t instanceof String) {
                return ((String) t).getBytes();
            }
            return this.objectMapper.writeValueAsBytes(t);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {

        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
