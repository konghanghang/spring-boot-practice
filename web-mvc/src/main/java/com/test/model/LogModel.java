package com.test.model;

import lombok.Data;

@Data
public class LogModel {

    private String action;

    private String username;

    private String requestParams;

    private String result;

    private String url;

}
