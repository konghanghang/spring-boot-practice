package com.test.shiro.web.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

public class WebRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getPermissionsByUsername(username);
         SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 模拟从数据库获取权限信息
     * @param username
     * @return
     */
    private Set<String> getPermissionsByUsername(String username) {
        Set<String> set = new HashSet<>();
        set.add("user:delete");
        set.add("user:add");
        return set;
    }

    /**
     * 模拟从数据库获取用户角色
     * @param username
     * @return
     */
    private Set<String> getRolesByUsername(String username) {
        Set<String> set = new HashSet<>();
        set.add("admin");
        set.add("user");
        return set;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = getPasswordByUsername(username);
        if (password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("test",password,getName());
        return authenticationInfo;
    }

    private String getPasswordByUsername(String username) {
        System.out.println(username);
        return "1234567";
    }
}
