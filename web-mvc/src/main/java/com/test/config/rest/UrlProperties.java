package com.test.config.rest;

import lombok.Data;

@Data
public class UrlProperties {

    private String url;
    private String method;
    private Integer connectTimeout;
    private Integer readTimeout;
    private Integer writeTimeout;

}
