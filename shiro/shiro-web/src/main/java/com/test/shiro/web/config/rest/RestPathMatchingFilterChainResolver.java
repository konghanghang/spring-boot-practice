package com.test.shiro.web.config.rest;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 在restShiroFilterFactoryBean中调用
 * @author konghang
 */
public class RestPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {

    private static final Logger log = LoggerFactory.getLogger(RestPathMatchingFilterChainResolver.class);

    public RestPathMatchingFilterChainResolver() {
        super();
    }

    public RestPathMatchingFilterChainResolver(FilterConfig filterConfig) {
        super(filterConfig);
    }

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        String requestURI = getPathWithinApplication(request);
        String[] urls = null;
        for (String pathPattern : filterChainManager.getChainNames()) {
            urls = pathPattern.split("--");
            if (urls.length == 2) {
                // 分割出url+httpMethod,判断httpMethod和request请求的method是否一致,不一致直接false
                if (WebUtils.toHttp(request).getMethod().toUpperCase().equals(urls[1].toUpperCase())) {
                    pathPattern = urls[0];
                }
            }
            if (pathMatches(pathPattern, requestURI)) {
                if (log.isTraceEnabled()) {
                    log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " +
                            "Utilizing corresponding filter chain...");
                }
                if (urls.length == 2) {
                    pathPattern = pathPattern.concat("--").concat(WebUtils.toHttp(request).getMethod().toUpperCase());
                }
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }
        return null;
    }

}
