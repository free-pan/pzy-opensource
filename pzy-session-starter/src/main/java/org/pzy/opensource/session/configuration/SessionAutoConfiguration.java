package org.pzy.opensource.session.configuration;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.session.properties.SessionProperties;
import org.pzy.opensource.session.util.SpringSessionUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author pan
 * @date 2020-01-27
 */
@Configuration
@Slf4j
@EnableConfigurationProperties({SessionProperties.class})
public class SessionAutoConfiguration {

    @Autowired
    private SessionProperties sessionProperties;

    @Value("${component.cross.enable:false}")
    private boolean crossEnable;

    @PostConstruct
    public void init() {
        log.debug("已启用基于redis的session组件!");
        log.debug("如果需要实现在线会话(用户)的管理,请自行实现:org.pzy.opensource.session.service.OnlineUserService接口,并将该Service的实例以及org.pzy.opensource.session.listener.SessionExpiredEventListener的实例一同注入到spring容器中!");
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redisson(@Value("classpath:/redisson.yml") Resource configFile) throws IOException {
        Config config = Config.fromYAML(configFile.getInputStream());
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        log.debug("使用RedissonClient实例对RedisConnectionFactory进行初始化!");
        return new RedissonConnectionFactory(redisson);
    }

    @Bean
    @ConditionalOnMissingBean
    SpringSessionUtil springSessionUtil() {
        return SpringSessionUtil.INSTANCE;
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        DefaultCookieSerializer sessionIdCookie = new DefaultCookieSerializer();
        sessionIdCookie.setCookieName(sessionProperties.getCookieName());
        sessionIdCookie.setUseHttpOnlyCookie(true);
        // Cookie 的 SameSite 属性详解: http://www.ruanyifeng.com/blog/2019/09/cookie-samesite.html
        if (this.crossEnable) {
            sessionIdCookie.setSameSite(null);
        }

        CookieHttpSessionIdResolver cookieHttpSessionIdResolver = new CookieHttpSessionIdResolver();
        cookieHttpSessionIdResolver.setCookieSerializer(sessionIdCookie);
        return cookieHttpSessionIdResolver;
    }
}
