package org.pzy.opensource.security.properties;

import lombok.Data;

/**
 * @author pan
 * @date 2020-01-27
 */
@Data
public class PathFilterChain {

    /**
     * uri路径(支持ant路径表达式)
     */
    private String path;
    /**
     * 过滤器. 多个使用,号分割
     */
    private String filters;
}
