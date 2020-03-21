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
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.pzy.opensource.security.domain.bo.PermissionInfoBO;
import org.pzy.opensource.security.domain.bo.ShiroUserBO;
import org.pzy.opensource.security.service.ShiroWinterUserService;
import org.pzy.opensource.security.util.ShiroFilterUtil;
import org.pzy.opensource.web.util.HttpResponseUtl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用于uri鉴权. 并增加跨域支持
 *
 * @author 潘志勇
 * @date 2019-02-12
 */
@Slf4j
public class CrossUriPermissionFilter extends AbstractWinterShiroFormAuthenticationFilter {

    /**
     * 用于查询用户权限
     */
    private ShiroWinterUserService shiroWinterUserService;
    /**
     * 发现用户无访问当前uri的权限之后,转入的url
     */
    private String forbiddenRedirectUrl;

    /**
     * 构造函数
     *
     * @param shiroWinterUserService 用于查询用户权限
     * @param forbiddenRedirectUrl   发现用户无访问当前uri的权限之后,转入的url
     */
    public CrossUriPermissionFilter(ShiroWinterUserService shiroWinterUserService, String forbiddenRedirectUrl) {
        super("crossUriAuthc");
        this.shiroWinterUserService = shiroWinterUserService;
        this.forbiddenRedirectUrl = forbiddenRedirectUrl;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 是否为OPTIONS请求
        boolean isOptionsMethod = ShiroFilterUtil.requestIsOptionsMethod(request);
        if (log.isDebugEnabled()) {
            log.debug("当前请求方式为:[{}], uri地址:[{}], 是否是OPTIONS请求:{}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), isOptionsMethod);
        }
        if (isOptionsMethod) {
            // options请求直接放行
            return true;
        }
        // 当前是否已经登录或当前是否登录请求
        boolean isAuthenticated = super.isAccessAllowed(request, response, mappedValue);
        if (!isAuthenticated) {
            // 当前未登录,则拒绝
            return false;
        } else {
            // 当前已登录
            String method = httpServletRequest.getMethod();
            String requestUri = httpServletRequest.getRequestURI();
            Subject subject = super.getSubject(request, response);
            ShiroUserBO shiroUserBO = (ShiroUserBO) subject.getPrincipal();
            // 获取当前用户的所有uri权限
            List<PermissionInfoBO> permissionInfoList = this.shiroWinterUserService.loadPermissionByUsername(shiroUserBO);
            // uri鉴权,有权限则放行,无权限则执行onAccessDenied逻辑
            boolean hasPermission = ShiroFilterUtil.hasPermission(permissionInfoList, method, requestUri);
            return hasPermission;
        }
    }

    /**
     * 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，直接返回即可
     * <p>
     * isAccessAllowed和onAccessDenied的执行关系由onPreHandle决定
     *
     * @param request
     * @param response
     * @return 返回true, 则会继续执行下一个过滤器
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.saveRequest(request);
        HttpResponseUtl.allowCross(response, request);
        WebUtils.issueRedirect(request, response, this.forbiddenRedirectUrl);
        return false;
    }


    @Override
    public String getDesc() {
        return "用于uri鉴权. 并增加跨域支持.";
    }
}
