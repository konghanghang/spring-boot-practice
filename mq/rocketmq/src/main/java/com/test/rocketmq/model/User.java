package com.test.rocketmq.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 消息示例的用户载荷。保留旧 SecurityProperties.User 的 JSON 字段，
 * 避免消息协议依赖 Spring Boot 内部配置类型。
 */
public class User {
    private String name = "user";
    private String password = UUID.randomUUID().toString();
    private List<String> roles = new ArrayList<>();
    private boolean passwordGenerated = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
            this.passwordGenerated = false;
        }
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = new ArrayList<>(roles);
    }

    public boolean isPasswordGenerated() {
        return passwordGenerated;
    }

    public void setPasswordGenerated(boolean passwordGenerated) {
        this.passwordGenerated = passwordGenerated;
    }
}
