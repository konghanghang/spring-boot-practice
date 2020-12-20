package com.ioc.config;

import com.ioc.bean.ProfileBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;

@PropertySource("classpath:/profile.properties")
@Configuration
public class MainConfigOfProfile implements EmbeddedValueResolverAware {

    @Value("${name}")
    private String name;

    private Integer age;

    @Profile("test")
    @Bean("profileBean01")
    public ProfileBean profileBean01(){
        ProfileBean profileBean = new ProfileBean();
        profileBean.setName(name);
        profileBean.setAge(age);
        return profileBean;
    }

    @Profile("dev")
    @Bean("profileBean02")
    public ProfileBean profileBean02(@Value("${name2}") String name2){
        ProfileBean profileBean = new ProfileBean();
        profileBean.setName(name2);
        profileBean.setAge(age);
        return profileBean;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        final String s = resolver.resolveStringValue("${age}");
        age = Integer.valueOf(s);
    }
}
