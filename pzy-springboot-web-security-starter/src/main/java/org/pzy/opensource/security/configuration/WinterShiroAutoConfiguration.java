/*
 * Copyright (c) [2019] [潘志勇]
 *    [pzy-opensource] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 */

package org.pzy.opensource.security.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.pzy.opensource.security.properties.PathFilterChain;
import org.pzy.opensource.security.properties.WinterShiroProperties;
import org.pzy.opensource.security.shiro.CustomShiroFilterBoxTemplate;
import org.pzy.opensource.security.shiro.realm.WinterRealmTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.*;

/**
 * 安全组件自动配置
 *
 * @author pan
 * @date 2020-01-26
 */
@Configuration
@Slf4j
@EnableConfigurationProperties({WinterShiroProperties.class})
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "component.security.enable", havingValue = "true", matchIfMissing = true)
public class WinterShiroAutoConfiguration {

    private static final List<String> STATIC_RESOURCE_URI_LIST = Arrays.asList("/**.ico", "/**.jpg", "/**.png", "/**.html", "/**.js", "/**.css", "/pzy-swagger-ui", "/v2/api-docs");

    /**
     * 错误页地址
     */
    @Value("${server.error.path:${error.path:/error}}")
    private String errorUri;

    @Autowired
    private WinterShiroProperties winterShiroProperties;

    /**
     * 自定义shiro过滤器
     */
    @Autowired(required = false)
    private CustomShiroFilterBoxTemplate customShiroFilterBox;

    @Autowired(required = false)
    private List<WinterRealmTemplate> realmList;

    @PostConstruct
    public void init() {
        if (CollectionUtils.isEmpty(realmList)) {
            throw new RuntimeException("请至少指定一个org.pzy.opensource.security.shiro.realm.WinterRealmTemplate实例!");
        }
        log.debug("权限组件已被启用!");
    }

    /**
     * session管理器(此处配置的是web容器自带的session管理器)
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    SessionManager sessionManager() {
        ServletContainerSessionManager sessionManager = new ServletContainerSessionManager();
        return sessionManager;
    }

    /**
     * 认证管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityManager securityManager(SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 记住我配置
        SimpleCookie rememberMeCookie = new SimpleCookie(this.winterShiroProperties.getRememberMeCookieName());
        rememberMeCookie.setHttpOnly(true);
        // 设置cookie失效时间
        rememberMeCookie.setMaxAge(new Long(this.winterShiroProperties.getRememberMeMaxAge().getSeconds()).intValue());
        // 记住我配置
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        // 设置加密秘钥. 可用如下代码生成一个秘钥
        //KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //SecretKey deskey = keygen.generateKey();
        //System.out.println(Base64.encodeToString(deskey.getEncoded()));
        cookieRememberMeManager.setCipherKey(Base64.decode(winterShiroProperties.getRememberMeBase64CipherKey()));
        cookieRememberMeManager.setCookie(rememberMeCookie);
        securityManager.setRememberMeManager(cookieRememberMeManager);


        if (!CollectionUtils.isEmpty(this.realmList)) {
            ArrayList<Realm> realmCollection = new ArrayList<>(realmList.size());
            this.realmList.stream().forEach(item -> {
                realmCollection.add(item);
            });
            securityManager.setRealms(realmCollection);
        }
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean
    @ConditionalOnMissingBean
    MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(securityManager);
        return methodInvokingFactoryBean;
    }

    /**
     * 加入注解的使用，不加入这个注解不生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     */
    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 自定义shiro过滤器
        if (null != this.customShiroFilterBox && !CollectionUtils.isEmpty(this.customShiroFilterBox.getWinterShiroAccessControlFilters())) {
            Map<String, Filter> customFiltersMap = new HashMap<>();
            StringBuilder stringBuilder = new StringBuilder("\r\n自定义shiro过滤器:\r\n");
            this.customShiroFilterBox.getWinterShiroAccessControlFilters().stream().forEach(shiroFilter -> {
                customFiltersMap.put(shiroFilter.getFilterName(), shiroFilter);
                stringBuilder.append(shiroFilter.getFilterName()).append(": ").append(shiroFilter.getDesc()).append("\r\n");
            });
            log.debug(stringBuilder.toString());
            shiroFilterFactoryBean.setFilters(customFiltersMap);
        }

        Map<String, String> filterChainMap = new LinkedHashMap<>(20);
        String anonFilter = "anon";

        if (StringUtils.isEmpty(winterShiroProperties.getLoginUrl())) {
            throw new RuntimeException("权限组件需要配置:登录请求后端地址.");
        }
        filterChainMap.put(winterShiroProperties.getLoginUrl(), anonFilter);

        // 登出相关uri无需授权
        if (StringUtils.isEmpty(winterShiroProperties.getLogoutUrl())) {
            throw new RuntimeException("权限组件需要配置:登出请求后端地址.");
        }
        filterChainMap.put(winterShiroProperties.getLogoutUrl(), anonFilter);

        // 错误页无需权限校验
        if (!StringUtils.isEmpty(errorUri)) {
            filterChainMap.put(this.errorUri, anonFilter);
        }
        // 静态资源无需权限校验
        for (String anonUriPattern : STATIC_RESOURCE_URI_LIST) {
            filterChainMap.put(anonUriPattern, anonFilter);
        }
        // 额外配置的uri无需权限校验
        if (!CollectionUtils.isEmpty(this.winterShiroProperties.getAnonUriPatternColl())) {
            StringBuilder stringBuilder = new StringBuilder("\r\n自定义无需鉴权的路径:\r\n");
            for (String anonUriPattern : this.winterShiroProperties.getAnonUriPatternColl()) {
                filterChainMap.put(anonUriPattern, anonFilter);
                stringBuilder.append(anonUriPattern).append(", ");
            }
            log.debug(stringBuilder.toString());
        }

        // 注入自定义路径过滤器链
        List<PathFilterChain> otherShiroFilterChain = winterShiroProperties.getShiroFilterChain();
        if (!CollectionUtils.isEmpty(otherShiroFilterChain)) {
            StringBuilder stringBuilder = new StringBuilder("\r\n自定义shiro过滤器链:\r\n");
            for (PathFilterChain item : otherShiroFilterChain) {
                filterChainMap.put(item.getPath(), item.getFilters());
                stringBuilder.append(item.getPath()).append(" --> ").append(item.getFilters()).append("\r\n");
            }
            log.debug(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder("\r\nshiro总过滤链表:\r\n");
        for (Map.Entry<String, String> entry : filterChainMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\r\n");
        }
        log.debug(stringBuilder.toString());
        //登录
//        shiroFilterFactoryBean.setLoginUrl(winterShiroProperties.getNeedLoginUri());
        if (!StringUtils.isEmpty(winterShiroProperties.getLoginSuccessUrl())) {
            //首页(登录成功之后转入的界面)
            shiroFilterFactoryBean.setSuccessUrl(winterShiroProperties.getLoginSuccessUrl());
        }
        //错误页面，认证不通过跳转页面
//        shiroFilterFactoryBean.setUnauthorizedUrl(winterShiroProperties.getUnauthorizedUrl());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);

        return shiroFilterFactoryBean;
    }

}
