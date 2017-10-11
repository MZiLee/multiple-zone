package com.multiple.zone.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
* @Description
* @Author lichao
* @Date 2017年10月11日14:33:27
*/
@Configuration
public class ShiroConfiguration {

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }


    @Bean(name = "shiroAuthorizeReam")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroAuthorizeReam shiroAuthorizeReam() {
        ShiroAuthorizeReam realm = new ShiroAuthorizeReam();
        realm.setCacheManager(cacheManager());
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }


    @Bean(name = "cacheManager")
    public RedisCacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        return cacheManager;
    }

    @Bean
    public RedisSessionRepository sessionRepository() {
        RedisSessionRepository sessionRepository = new RedisSessionRepository();
        return sessionRepository;
    }


    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationInterval(1800000);
        sessionManager.setSessionDAO(sessionRepository());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        return sessionManager;
    }


    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        //cookie有效期30天
        cookie.setMaxAge(2592000);
        cookie.setHttpOnly(true);
        return cookie;
    }

    @Bean(name = "securityManager")
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //注入自定义Realm
        securityManager.setRealm(shiroAuthorizeReam());
        //注入缓存管理器
        securityManager.setCacheManager(cacheManager());
        //注入session管理器
        securityManager.setSessionManager(sessionManager());
        //记住我
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }


    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    @Bean
    SessionExpiredFilter sessionExpiredFilter(){
        SessionExpiredFilter sessionExpiredFilter=new SessionExpiredFilter();
        return sessionExpiredFilter;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
//        LogoutFilter logoutFilter = new LogoutFilter();
//        logoutFilter.setRedirectUrl("/index.html");
        filters.put("user", new SessionExpiredFilter());
//        filters.put("logout", new com.gome.performance.web.config.shiro.LogoutFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionManager = new LinkedHashMap<String, String>();
        filterChainDefinitionManager.put("/index.html","anon");
        filterChainDefinitionManager.put("/scanCodeLogin.html","anon");
        filterChainDefinitionManager.put("/static/**","anon");
        filterChainDefinitionManager.put("/js/**","anon");
        //获取验证码
        filterChainDefinitionManager.put("/defaultKaptcha","anon");
        filterChainDefinitionManager.put("/login","anon");
        filterChainDefinitionManager.put("/scanCodeLogin","anon");
        filterChainDefinitionManager.put("/logout","anon");
        filterChainDefinitionManager.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);

        shiroFilterFactoryBean.setLoginUrl("/index.html");
        shiroFilterFactoryBean.setSuccessUrl("/main.html");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        return shiroFilterFactoryBean;
    }

}
