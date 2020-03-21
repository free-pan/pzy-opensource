package org.pzy.opensource.security.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.util.WebUtils;
import org.pzy.opensource.security.util.ShiroFilterUtil;
import org.pzy.opensource.web.util.HttpResponseUtl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 功能和shiro的authc过滤器一样,只是增加了跨域支持!
 *
 * @author pan
 * @date 2020-01-29
 */
@Slf4j
public class CrossFormAuthenticationFilter extends AbstractWinterShiroFormAuthenticationFilter {
    /**
     * 未认证(未登录)时转向的url
     */
    private String unauthorizedRedirectUrl;

    /**
     * 构造函数
     *
     * @param unauthorizedRedirectUrl 未认证(未登录)时转向的url
     */
    public CrossFormAuthenticationFilter(String unauthorizedRedirectUrl) {
        super("crossAuthc");
        this.unauthorizedRedirectUrl = unauthorizedRedirectUrl;
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
        saveRequest(request);
        HttpResponseUtl.allowCross(response, request);
        WebUtils.issueRedirect(request, response, unauthorizedRedirectUrl);
    }

    @Override
    public String getDesc() {
        return "功能和shiro的authc过滤器一样,只是增加了跨域支持!";
    }


}
