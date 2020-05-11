package com.test.shiro.web.config;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import java.util.*;

/**
 * 过滤器以及过滤url管理
 * @author konghang
 */
@Component
public class ShiroFilterChainManager {

    /**
     * 初始化获取过滤链
     */
    public Map<String, Filter> initFilters() {
        Map<String, Filter> filters = new LinkedHashMap<>();
        return filters;
    }

    /**
     * 初始化获取过滤链规则
     * @return
     */
    public Map<String,String> initFilterChain() {
        Map<String,String> filterChain = new LinkedHashMap<>();
        // -------------anon 默认过滤器忽略的URL
        List<String> defaultAnon = Arrays.asList("/css/**","/js/**","/swagger-ui.html","/webjars/**","/swagger-resources/**","/v2/api-docs");
        defaultAnon.forEach(ignored -> filterChain.put(ignored,"anon"));
        // -------------dynamic 动态URL
        List<RolePermRule> rolePermRules = new ArrayList<>();
        wrap(rolePermRules);
        rolePermRules.stream().forEach(rule -> {
            StringBuilder Chain = rule.toFilterChain();
            if (null != Chain) {
                // 如果不存在则put
                filterChain.putIfAbsent(rule.getUrl(),Chain.toString());
            }
        });
        List<String> user = Arrays.asList("/login","/test--GET","/test--POST");
        user.stream().forEach(ignored -> filterChain.put(ignored,"anon"));
        // -------------jwt jwt相关,无角色需求
        List<String> defaultJwt = Arrays.asList("/v1/**");
        defaultJwt.stream().forEach(jwt -> filterChain.put(jwt, "jwt"));
        filterChain.put("/**", "anon");
        return filterChain;
    }

    /**
     * /v1/user/login=anon
     * @param rolePermRules
     */
    private void wrap(List<RolePermRule> rolePermRules){
        RolePermRule rolePermRule = new RolePermRule();
        rolePermRule.setUrl("/api/user/login");
        rolePermRule.setNeedRoles("role_anon");
        rolePermRules.add(rolePermRule);
        RolePermRule rule = new RolePermRule();
        rule.setUrl("/index--POST");
        rule.setNeedRoles("role_anon");
        rolePermRules.add(rule);
        RolePermRule rolePermRule1 = new RolePermRule();
        rolePermRule1.setUrl("/api/user/register");
        rolePermRule1.setNeedRoles("role_anon");
        rolePermRules.add(rolePermRule1);
    }

    class RolePermRule {

        /**
         * 资源URL
         */
        private String url;
        /**
         * 访问资源所需要的角色列表，多个列表用逗号间隔
         */
        private String needRoles;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getNeedRoles() {
            return needRoles;
        }

        public void setNeedRoles(String needRoles) {
            this.needRoles = needRoles;
        }

        /**
         * 将url needRoles 转化成shiro可识别的过滤器链：url=jwt[角色1、角色2、角色n]
         * @return
         */
        public StringBuilder toFilterChain() {

            if (StringUtils.isEmpty(url)){
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            Set<String> setRole = new HashSet<>();
            setRole.addAll(Arrays.asList(this.getNeedRoles().split(",")));

            // 约定若role_anon角色拥有此uri资源的权限,则此uri资源直接访问不需要认证和权限
            if (!StringUtils.isEmpty(this.getNeedRoles()) && setRole.contains("role_anon")) {
                stringBuilder.append("anon");
            }
            //  其他自定义资源uri需通过jwt认证和角色认证
            if (!StringUtils.isEmpty(this.getNeedRoles()) && !setRole.contains("role_anon")) {
                stringBuilder.append("jwt"+"["+this.getNeedRoles()+"]");
            }

            return stringBuilder.length() > 0 ? stringBuilder : null;
        }
    }
}
