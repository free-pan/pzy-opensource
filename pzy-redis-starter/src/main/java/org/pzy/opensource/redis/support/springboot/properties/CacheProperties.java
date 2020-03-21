package org.pzy.opensource.redis.support.springboot.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author pzy
 * @date 2019/1/10
 */
@ConfigurationProperties(prefix = "component.cache", ignoreInvalidFields = true)
@Setter
@Getter
@ToString
public class CacheProperties {

    /**
     * 缓存key的命名空间. 默认值: winter-cache:
     */
    private String cacheKeyNamespace = "winter-cache:";

    /**
     * 默认的缓存失效时间.(单位:秒) 默认:300秒(5分钟)
     */
    private Long defaultExpire;

    /**
     * 独立的缓存项配置
     */
    private List<CacheItemConfig> cacheItems;

    public CacheProperties() {
        this.defaultExpire = 300L;
    }
}
