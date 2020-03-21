package org.pzy.opensource.security.shiro.filter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pan
 * @date 2020-01-28
 */
public interface WinterShiroFilter extends Filter {

    /**
     * 获取过滤器的名称
     *
     * @return
     */
    String getFilterName();

    /**
     * 获取过滤器描述信息
     *
     * @return
     */
    String getDesc();

    /**
     * 允许跨域
     *
     * @param request
     * @param response
     */
    void allowCross(HttpServletRequest request, HttpServletResponse response);

    /**
     * 清除缓存
     *
     * @param response
     */
    void disableCache(HttpServletResponse response);
}
