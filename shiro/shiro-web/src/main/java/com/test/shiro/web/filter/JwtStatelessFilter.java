package com.test.shiro.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.shiro.web.config.rest.JwtStatelessToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt无状态过滤器
 * @author konghang
 */
public class JwtStatelessFilter extends RestPathMatchingFilter {

    private static final String OPTIONS = "options";
    private static final String NULL = "null";
    private static final String AUTHORIZATION = "Authorization";

    /**
     * isAccessAllowed 和下边的 onAccessDenied 在 RestPathMatchingFilter 中的 onPreHandle 方法中被调用
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request,response);
        return subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String method = httpRequest.getMethod();
        // 过滤跨域的options请求
        if(OPTIONS.equalsIgnoreCase(method)){
            return true;
        }
        Subject subject = getSubject(request,response);
        // 获取Authorization字段
        String authorization = httpRequest.getHeader(AUTHORIZATION);
        if (authorization!=null && !NULL.equalsIgnoreCase(authorization)) {
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            String username = getUsername(authorization);
            JwtStatelessToken token = new JwtStatelessToken(username,authorization);
            subject.login(token);
            // 通过isPermitted 才能调用doGetAuthorizationInfo方法获取权限信息
            //getSubject(request, response).isPermitted(httpRequest.getRequestURI());
            return true;
        } else {
            authorizationFail(response, "USER_UNAUTHORIZED");
        }
        return false;
    }

    private String getUsername(String authorization) {
        return null;
    }

    /**
     * 将请求返回到 /401
     */
    private void authorizationFail(ServletResponse response, String message) throws Exception {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json;charset=UTF-8");
        servletResponse.setHeader("Access-Control-Allow-Origin","*");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}
