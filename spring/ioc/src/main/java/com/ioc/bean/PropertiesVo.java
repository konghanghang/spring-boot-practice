package com.ioc.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class PropertiesVo {

    @Value("${name}")
    private String name;

}
