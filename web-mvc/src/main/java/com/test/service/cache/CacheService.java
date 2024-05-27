package com.test.service.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.UUID;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author yslao@outlook.com
 * @since 2022/4/19
 */
@Slf4j
@Service
public class CacheService {

    @Resource
    private Caffeine caffeine;

    @Cacheable(value = "user_cache", key = "#id", unless = "#result == null")
    public String getId(String id) {
        String s = UUID.randomUUID().toString();
        log.info("get uuid:{}", s);
        return s;
    }

    @CachePut(value = "user_cache", key = "#id", unless = "#result == null")
    public String update(String id) {
        String s = UUID.randomUUID().toString();
        log.info("update uuid:{}", s);
        return s;
    }

    @CacheEvict(value = "user_cache", key = "#id")
    public void deleteById(String id) {
        String s = caffeine.toString();
        log.info("delete by id:{}, s:{}", id, s);
    }

}
