package com.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.interceptor.GlobalInterceptor;
import com.test.interceptor.RequestArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义实现mvc配置
 * 完全接管WebMvc自动配置，可以在项目中创建一个注解了@EnableWebMvc的配置类
 * @author konghang
 */
@Configuration
@EnableWebMvc
public class SpringMvcConfigure implements WebMvcConfigurer {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private GlobalInterceptor globalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor);
    }

    /**
     * 配置静态访问资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/webjars/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestArgumentResolver(objectMapper));
    }
}
