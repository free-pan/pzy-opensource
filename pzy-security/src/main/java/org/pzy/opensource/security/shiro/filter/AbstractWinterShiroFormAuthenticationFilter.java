package org.pzy.opensource.security.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.pzy.opensource.web.util.HttpResponseUtl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该过滤器要求至少已登录
 *
 * @author pan
 * @date 2020-01-28
 */
@Slf4j
public abstract class AbstractWinterShiroFormAuthenticationFilter extends FormAuthenticationFilter implements WinterShiroFilter {

    private String filterName;

    /**
     * @param filterName 过滤器的名称
     */
    public AbstractWinterShiroFormAuthenticationFilter(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public String getFilterName() {
        return this.filterName;
    }

    @Override
    public void allowCross(HttpServletRequest request, HttpServletResponse response) {
        HttpResponseUtl.allowCross(response, request);
    }

    @Override
    public void disableCache(HttpServletResponse response) {
        HttpResponseUtl.disableCache(response);
    }

}
