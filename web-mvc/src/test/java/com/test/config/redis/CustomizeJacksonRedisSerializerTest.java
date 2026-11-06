package com.test.config.redis;

import com.iminling.common.json.JsonUtil;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomizeJacksonRedisSerializerTest {

    @Test
    void preservesUtf8StringsAndEmptyValues() {
        var serializer = new CustomizeJacksonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(JsonUtil.getInstant());

        assertArrayEquals("中文缓存".getBytes(StandardCharsets.UTF_8), serializer.serialize("中文缓存"));
        assertEquals("中文缓存", serializer.deserialize(serializer.serialize("中文缓存")));
        assertArrayEquals(new byte[0], serializer.serialize(null));
        assertNull(serializer.deserialize(new byte[0]));
        assertNull(serializer.deserialize(null));
    }

    @Test
    void readsExistingJsonAndPreservesLongAndDateFormat() {
        var serializer = new CustomizeJacksonRedisSerializer<>(CachedValue.class);
        serializer.setObjectMapper(JsonUtil.getInstant());
        var value = new CachedValue(9007199254740993L, LocalDateTime.of(2024, 5, 18, 12, 30, 0));
        byte[] legacyJson = "{\"id\":\"9007199254740993\",\"createdAt\":\"2024-05-18 12:30:00\"}"
                .getBytes(StandardCharsets.UTF_8);

        assertEquals(value, serializer.deserialize(legacyJson));
        assertEquals(value, serializer.deserialize(serializer.serialize(value)));
        var json = JsonUtil.getInstant().readTree(serializer.serialize(value));
        assertEquals("9007199254740993", json.get("id").asString());
        assertTrue(json.get("id").isString());
        assertEquals("2024-05-18 12:30:00", json.get("createdAt").asString());
    }

    public record CachedValue(Long id, LocalDateTime createdAt) {}
}
