package com.test.cloud.service;

import com.test.api.ProductInfo;
import com.test.api.client.ProductInfoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yslao@outlook.com
 * @since 2022/4/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerProductInfoService {

    private final ProductInfoClient productInfoClient;

    public Integer add(ProductInfo info) {
        return productInfoClient.add(info);
    }

    public ProductInfo get(Integer id) {
        return productInfoClient.get(id);
    }

}
