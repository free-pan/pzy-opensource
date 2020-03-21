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

package org.pzy.opensource.redis.support.springboot.configuration;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.redis.support.springboot.aop.WinterLockAop;
import org.pzy.opensource.redis.support.springboot.cache.WinterCacheKeyGenerator;
import org.pzy.opensource.redis.support.springboot.properties.CacheItemConfig;
import org.pzy.opensource.redis.support.springboot.properties.CacheProperties;
import org.pzy.opensource.redis.support.springboot.redis.StringKeyObjectByteArrayValueRedisTemplate;
import org.pzy.opensource.redis.support.springboot.redis.StringKeyObjectJsonValueRedisTemplate;
import org.pzy.opensource.redis.support.util.*;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * redis操作帮助类自动实例化
 *
 * @author pan
 * @date 2019-03-23
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class WinterRedisAutoConfiguration {

    @Autowired
    private CacheProperties cacheProperties;

    @Bean(destroyMethod = "shutdown")
    @Primary
    public RedissonClient redisson(@Value("classpath:/redisson.yml") Resource configFile) throws IOException {
        Config config = Config.fromYAML(configFile.getInputStream());
        return Redisson.create(config);
    }

    @Bean
    @Primary
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        log.debug("使用RedissonClient实例对RedisConnectionFactory进行初始化!");
        return new RedissonConnectionFactory(redisson);
    }

    @Bean
    @ConditionalOnMissingBean
    StringKeyObjectJsonValueRedisTemplate stringKeyObjectValueRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringKeyObjectJsonValueRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    StringKeyObjectByteArrayValueRedisTemplate stringKeyObjectByteArrayValueRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringKeyObjectByteArrayValueRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisUtil redisUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate, StringKeyObjectByteArrayValueRedisTemplate stringKeyObjectByteArrayValueRedisTemplate) {
        return RedisUtil.newInstance(redisTemplate, stringKeyObjectByteArrayValueRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisSortedSetUtil redisSortedSetUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate) {
        return RedisSortedSetUtil.newInstance(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisSetUtil redisSetUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate) {
        return RedisSetUtil.newInstance(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisPublishUtil redisPublishUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate) {
        return RedisPublishUtil.newInstance(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisListUtil redisListUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate) {
        return RedisListUtil.newInstance(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisIncrAndDecrUtil redisIncrAndDecrUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate) {
        return RedisIncrAndDecrUtil.newInstance(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisHashUtil redisHashUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate) {
        return RedisHashUtil.newInstance(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisDistributedLockUtil redisDistributedLockUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate) {
        return RedisDistributedLockUtil.newInstance(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedisDistributedLockAdvanceUtil redisDistributedLockAdvanceUtil(StringKeyObjectJsonValueRedisTemplate redisTemplate) {
        return RedisDistributedLockAdvanceUtil.newInstance(redisTemplate);
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @Primary
    CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        log.debug("使用的是基于redis的缓存!");
        // 默认缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration
                .ofSeconds(cacheProperties.getDefaultExpire()));
        if (log.isDebugEnabled()) {
            log.debug("redis缓存全局配置:");
            log.debug("redis 缓存默认超时时间(秒):{}", cacheProperties.getDefaultExpire());
            log.debug("redis 会将null结果也缓存!");
        }

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder = RedisCacheManager.builder(redisCacheWriter);
        redisCacheManagerBuilder.cacheDefaults(defaultCacheConfig);

        if (!CollectionUtils.isEmpty(cacheProperties.getCacheItems())) {
            // 为指定缓存项做独立配置
            Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>(cacheProperties.getCacheItems().size());
            for (CacheItemConfig cacheItemConfig : cacheProperties.getCacheItems()) {
                RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(cacheItemConfig.getExpiryTimeSecond())).disableCachingNullValues();
                cacheConfigurations.put(cacheItemConfig.getName(), redisCacheConfiguration);
                if (log.isDebugEnabled()) {
                    log.debug("@Cacheable注解value为[{}]的redis缓存有效期为:{} 秒", cacheItemConfig.getName(), cacheItemConfig.getExpiryTimeSecond());
                }
            }
            redisCacheManagerBuilder.withInitialCacheConfigurations(cacheConfigurations);
        }
        return redisCacheManagerBuilder.build();
    }

    /**
     * 替换默认的key生成方式
     *
     * @return
     */
    @Bean
    @Primary
    public KeyGenerator keyGenerator() {
        return new WinterCacheKeyGenerator(cacheProperties.getCacheKeyNamespace());
    }


    @Bean
    @ConditionalOnMissingBean
    WinterLockAop lockAop(RedissonClient redissonClient) {
        return new WinterLockAop(redissonClient);
    }
}
