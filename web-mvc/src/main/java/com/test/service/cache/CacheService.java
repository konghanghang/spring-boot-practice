package com.test.service.cache;

/**
 * @author yslao@outlook.com
 * @since 2022/4/19
 */
public interface CacheService {

    String getId(String id);
    String update(String id);
    void deleteById(String id);

}
