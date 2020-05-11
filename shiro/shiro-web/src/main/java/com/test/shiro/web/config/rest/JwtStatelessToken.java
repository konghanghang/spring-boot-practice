package com.test.shiro.web.config.rest;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义token
 * @author konghang
 */
public class JwtStatelessToken implements AuthenticationToken {

    private String username;
    private String token;

    public JwtStatelessToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    public void clear() {
        this.username = null;
        this.token = null;
    }
}
