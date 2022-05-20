package com.test.api;

import lombok.Data;

/**
 * @author yslao@outlook.com
 * @since 2022/4/24
 */
@Data
public class ProductInfo {
    private Integer productId;
    private String productName;
    private String productDesc;
    private String productIcon;
    private String productPrice;
    private String productStatus;
    private String productStock;
    private String productCategory;
    private String productCreateTime;
    private String productUpdateTime;
}
