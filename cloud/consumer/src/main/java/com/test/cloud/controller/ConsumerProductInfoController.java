package com.test.cloud.controller;

import com.iminling.core.annotation.EnableResolve;
import com.test.api.ProductInfo;
import com.test.api.client.ProductInfoClient;
import com.test.cloud.service.ConsumerProductInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yslao@outlook.com
 * @since 2022/4/24
 */
@EnableResolve
@RestController
@RequiredArgsConstructor
public class ConsumerProductInfoController implements ProductInfoClient {

    private final ConsumerProductInfoService consumerProductInfoService;

    @Override
    public Integer add(ProductInfo info) {
        return consumerProductInfoService.add(info);
    }

    @Override
    public ProductInfo get(Integer id) {
        return consumerProductInfoService.get(id);
    }
}
