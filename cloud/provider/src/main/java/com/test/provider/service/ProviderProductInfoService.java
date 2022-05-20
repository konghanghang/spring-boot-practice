package com.test.provider.service;

import cn.hutool.core.util.RandomUtil;
import com.test.api.ProductInfo;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yslao@outlook.com
 * @since 2022/4/25
 */
@Slf4j
@Service
public class ProviderProductInfoService {

    private final ConcurrentHashMap<Integer, ProductInfo> productInfoMap = new ConcurrentHashMap<>();

    public Integer add(ProductInfo info) {
        int randomInt = RandomUtil.randomInt();
        info.setProductId(randomInt);
        log.info("add product info: {}", info);
        productInfoMap.put(randomInt, info);
        return randomInt;
    }

    public ProductInfo get(Integer id) {
        return productInfoMap.get(id);
    }

}
