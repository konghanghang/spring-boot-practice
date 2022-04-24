package com.test.config;

import com.iminling.common.json.JsonUtil;
import com.test.config.redis.CustomizeJacksonRedisSerializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class MvcConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        CustomizeJacksonRedisSerializer<Object> jsonRedisSerializer = new CustomizeJacksonRedisSerializer<>(Object.class);
        jsonRedisSerializer.setObjectMapper(JsonUtil.getInstant());

        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(jsonRedisSerializer);
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashKeySerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /*@Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
            ).serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
            );
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(configuration).build();
    }
*/
}
