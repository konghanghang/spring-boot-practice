package com.test.shiro.web.config;

import com.test.shiro.web.config.rest.StatelessDefaultSubjectFactory;
import com.test.shiro.web.realm.WebRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * shiro配置
 * @author konghang
 */
@Configuration
public class ShiroConfig {

    /**
     * 密码匹配凭证管理器
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 散列的次数
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    /**
     * realm
     * @param matcher
     * @return
     */
    @Bean
    public Realm webRealm(HashedCredentialsMatcher matcher) {
        WebRealm webRealm = new WebRealm();
        // 设置密码凭证匹配器
        webRealm.setCredentialsMatcher(matcher);
        return webRealm;
    }

    /**
     * 安全管理器
     * @param realm
     * @param sessionManager
     * @return
     */
    @Bean
    public SecurityManager securityManager(Realm realm,
                                           SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(realm);
        securityManager.setSessionManager(sessionManager);
        // 在无状态应用中需要禁用将Subject状态持久化到会话,需要注意的是，禁用使用Sessions 作为存储策略的实现，
        // 但它没有完全地禁用Sessions。如果你的任何代码显式地调用subject.getSession()或subject.getSession(true)，
        // 一个session 仍然会被创建。https://blog.csdn.net/peterwanghao/article/details/8295620
        /*((DefaultSessionStorageEvaluator)(((DefaultSubjectDAO)securityManager
                .getSubjectDAO())
                .getSessionStorageEvaluator()))
                .setSessionStorageEnabled(false);*/
        DefaultSubjectDAO subjectDAO = (DefaultSubjectDAO)securityManager.getSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = (DefaultSessionStorageEvaluator) subjectDAO.getSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        StatelessDefaultSubjectFactory subjectFactory = new StatelessDefaultSubjectFactory(evaluator);
        securityManager.setSubjectFactory(subjectFactory);
        return securityManager;
    }

    /**
     * 会话管理器-定期检查session设置为false
     * @return
     */
    @Bean
    public SessionManager sessionManager(){
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }

    /**
     * shiroFilter
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroFilterChainManager chainManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 设置自定义filter
        shiroFilterFactoryBean.setFilters(chainManager.initFilters());
        // 设置过滤链
        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainManager.initFilterChain());
        return shiroFilterFactoryBean;
    }

    /**
     * Shiro生命周期处理器
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启shiro注解
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * 开启shiro注解
     * @param manager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

}
