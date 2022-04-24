package com.test.controller;

import com.test.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yslao@outlook.com
 * @since 2022/4/19
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheService cacheService;

    @GetMapping("/get")
    public String getId(String id) {
        return cacheService.getId(id);
    }

    @GetMapping("/update")
    public String update(String id) {
        return cacheService.update(id);
    }

    @GetMapping("/delete")
    public void deleteById(String id) {
        cacheService.deleteById(id);
    }

}
