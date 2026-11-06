package com.test.shiro.web.realm;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

public class WebRealmTest {

    @Test
    public void realm() {

        // 自定义realm
        WebRealm webRealm = new WebRealm();

        // 构建securityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(webRealm);

        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(1);
        webRealm.setCredentialsMatcher(credentialsMatcher);

        // 主题提交认证请求
        Subject subject = new Subject.Builder(defaultSecurityManager).buildSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("test","1234567");
        try {
            subject.login(token);
            assertTrue(subject.isAuthenticated());
            subject.checkRole("admin");
            subject.checkPermissions("user:delete", "user:add");
            subject.logout();
            assertFalse(subject.isAuthenticated());
            assertThrows(IncorrectCredentialsException.class,
                    () -> subject.login(new UsernamePasswordToken("test", "wrong-password")));
        } finally {
            subject.logout();
            defaultSecurityManager.destroy();
        }
    }

}
