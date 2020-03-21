package org.pzy.opensource.security.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.pzy.opensource.security.util.ShiroFilterUtil;
import org.pzy.opensource.web.util.HttpResponseUtl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能与shiro默认的user过滤器一致,验证失败,返回的是json
 *
 * @author pan
 * @date 2020-01-29
 */
@Slf4j
public class CrossUserFilter extends UserFilter implements WinterShiroFilter {

    /**
     * 未认证(未登录)时转向的url
     */
    private String unauthorizedRedirectUrl;

    /**
     * 构造方法
     *
     * @param unauthorizedRedirectUrl 未认证(未登录)时转向的url
     */
    public CrossUserFilter(String unauthorizedRedirectUrl) {
        this.unauthorizedRedirectUrl = unauthorizedRedirectUrl;
    }

    @Override
    public String getFilterName() {
        return "crossUser";
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.debug("请求信息[{}:{}]", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        if (ShiroFilterUtil.requestIsOptionsMethod(request)) {
            // options请求直接放行
            return true;
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.saveRequest(request);
        HttpResponseUtl.allowCross(response, request);
        WebUtils.issueRedirect(request, response, unauthorizedRedirectUrl);
    }

    @Override
    public String getDesc() {
        return "功能与shiro默认的user过滤器一致,验证失败,返回的是json!";
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
