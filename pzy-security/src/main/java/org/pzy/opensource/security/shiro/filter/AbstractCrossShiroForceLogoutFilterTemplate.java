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
import org.apache.shiro.web.util.WebUtils;
import org.pzy.opensource.web.util.HttpResponseUtl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 强制登出过滤器. 当被强制登出时, 会返回json数据,并设置http响应状态码为:401
 *
 * @author pan
 * @date 2020-01-26
 */
@Slf4j
public abstract class AbstractCrossShiroForceLogoutFilterTemplate extends AbstractWinterShiroFormAuthenticationFilter {

    /**
     * 存放于session中的强制踢出值的key,如果通过该key能够找到值,说明需要强制踢出
     */
    private String sessionForceLogoutKey;
    /**
     * 用户被强制踢出之后转入的url
     */
    private String forceLogoutRedirectUrl;

    /**
     * 构造方法
     *
     * @param filterName             过滤器名称
     * @param sessionForceLogoutKey  存放于session中的强制踢出值的key,如果通过该key能够找到值,说明需要强制踢出
     * @param forceLogoutRedirectUrl 用户被强制踢出之后转入的url
     */
    public AbstractCrossShiroForceLogoutFilterTemplate(String filterName, String sessionForceLogoutKey, String forceLogoutRedirectUrl) {
        super(filterName);
        this.sessionForceLogoutKey = sessionForceLogoutKey;
        this.forceLogoutRedirectUrl = forceLogoutRedirectUrl;
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.debug("请求信息[{}:{}]", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        boolean isOptionsMethod = super.isAccessAllowed(request, response, mappedValue);
        if (isOptionsMethod) {
            return true;
        }
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session == null) {
            return true;
        }
        // 如果当前session存在强制登出标识,则继续执行onAccessDenied的逻辑
        return session.getAttribute(this.sessionForceLogoutKey) == null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //执行登出操作
        WebUtils.saveRequest(request);
        HttpResponseUtl.allowCross(response, request);
        WebUtils.issueRedirect(request, response, this.forceLogoutRedirectUrl);
        return false;
    }

    public String getSessionForceLogoutKey() {
        return sessionForceLogoutKey;
    }

    @Override
    public String getDesc() {
        return "用于将session scope中存在[" + this.getSessionForceLogoutKey() + "]属性值的session强制登出.操作执行成功之后转入:" + this.forceLogoutRedirectUrl;
    }
}
