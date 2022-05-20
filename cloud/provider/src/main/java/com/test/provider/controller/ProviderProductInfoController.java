package com.test.provider.controller;

import com.iminling.core.annotation.EnableResolve;
import com.test.api.ProductInfo;
import com.test.api.client.ProductInfoClient;
import com.test.provider.service.ProviderProductInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yslao@outlook.com
 * @since 2022/4/24
 */
@EnableResolve
@RestController
@RequiredArgsConstructor
public class ProviderProductInfoController implements ProductInfoClient {

    private final ProviderProductInfoService providerProductInfoService;

    @Override
    public Integer add(ProductInfo info) {
        return providerProductInfoService.add(info);
    }

    @Override
    public ProductInfo get(Integer id) {
        return providerProductInfoService.get(id);
    }
}
