package com.test.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author yslao@outlook.com
 * @since 2022/4/19
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public Caffeine caffeineConfig() {
        return
            Caffeine.newBuilder()
                .maximumSize(10000).
                expireAfterWrite(60, TimeUnit.MINUTES);
    }

    @Primary
    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

}
