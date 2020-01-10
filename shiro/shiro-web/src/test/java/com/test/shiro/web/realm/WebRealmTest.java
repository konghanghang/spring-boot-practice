package com.test.shiro.web.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class WebRealmTest {

    @Test
    public void realm() {

        // 自定义realm
        WebRealm webRealm = new WebRealm();

        // 构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(webRealm);

        // 主题提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("test","1234567");
        subject.login(token);

        System.out.println(subject.isAuthenticated());

        subject.checkRole("admin");

        subject.checkPermissions("user:delete","user:add");
    }

}