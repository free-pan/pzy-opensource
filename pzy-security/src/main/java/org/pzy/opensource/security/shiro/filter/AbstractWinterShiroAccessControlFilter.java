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

package org.pzy.opensource.security.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.pzy.opensource.security.util.ShiroFilterUtil;
import org.pzy.opensource.web.util.HttpResponseUtl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义过滤器
 *
 * @author pan
 * @date 2020-01-26
 */
@Slf4j
public abstract class AbstractWinterShiroAccessControlFilter extends AccessControlFilter implements WinterShiroFilter {

    /**
     * 过滤器的名称,该名称不要与已有的过滤器名称重复
     */
    private String filterName;

    /**
     * 构造函数
     *
     * @param filterName 过滤器的名称[该名称不要与shiro默认的过滤器重复]
     */
    public AbstractWinterShiroAccessControlFilter(String filterName) {
        super.setName(filterName);
        this.filterName = filterName;
    }

    /**
     * 是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果返回false,则会继续执行onAccessDenied里的逻辑, 如果返回true,则不会再执行onAccessDenied里的逻辑.
     * <p>
     * isAccessAllowed和onAccessDenied的执行关系由onPreHandle决定
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isOptionOrLogin = ShiroFilterUtil.requestIsOptionsMethod(request) || super.pathsMatch(super.getLoginUrl(), httpServletRequest.getRequestURI());
        if (log.isDebugEnabled()) {
            log.debug("当前请求方式为:[{}], uri地址:[{}], 是否是OPTIONS请求或登录请求:{}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), isOptionOrLogin);
        }
        return isOptionOrLogin;
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
