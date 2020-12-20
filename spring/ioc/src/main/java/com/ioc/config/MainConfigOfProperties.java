package com.ioc.config;

import com.ioc.bean.PropertiesVo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/properties.properties")
@Configuration
public class MainConfigOfProperties {

    @Bean
    public PropertiesVo propertiesVo(){
        return new PropertiesVo();
    }

}
