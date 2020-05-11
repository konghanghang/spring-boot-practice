package com.test.shiro.web.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 参照org.apache.shiro.web.filter.AccessControlFilter
 * @author konghang
 */
public abstract class RestPathMatchingFilter extends PathMatchingFilter {

    public RestPathMatchingFilter(){}

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = this.getPathWithinApplication(request);
        // path: url--method eg: http://api/menu--GET   需要解析出path中的url和httpMethod
        String[] strings = path.split("--");
        if (strings.length <= 1) {
            // 分割出来只有URL
            return this.pathsMatch(strings[0], requestURI);
        } else {
            // 分割出url+httpMethod,判断httpMethod和request请求的method是否一致,不一致直接false
            String httpMethod = WebUtils.toHttp(request).getMethod().toUpperCase();
            return httpMethod.equals(strings[1].toUpperCase()) && this.pathsMatch(strings[0], requestURI);
        }
    }

    protected Subject getSubject(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject();
    }

    /**
     * 允许访问
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    protected abstract boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception;

    /**
     * 拒绝访问
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected abstract boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception;

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return onAccessDenied(request, response);
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response, mappedValue);
    }

}
