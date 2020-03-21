package org.pzy.opensource.redis.support.springboot.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author pzy
 * @date 2019/1/10
 */
@Setter
@Getter
@ToString
public class CacheItemConfig implements Serializable {

    private static final long serialVersionUID = -1370867291179230988L;
    /**
     * 缓存容器名称(对应@Cacheable(value='对应这个'))
     */
    private String name;
    /**
     * 缓存失效时间(单位:秒).默认:5分钟
     */
    private long expiryTimeSecond;

    public CacheItemConfig() {
        expiryTimeSecond = 300;
    }
}
