package com.test.api.client;

import com.test.api.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author yslao@outlook.com
 * @since 2022/4/24
 */
@FeignClient(name = "provider", contextId = "productInfo")
public interface ProductInfoClient {

    @PostMapping("/product/add")
    Integer add(@RequestBody ProductInfo info);

    @GetMapping("/product/{id}")
    ProductInfo get(@PathVariable(name = "id") Integer id);

}
