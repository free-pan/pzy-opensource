package org.pzy.opensource.security.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.pzy.opensource.security.domain.bo.PermissionInfoBO;
import org.pzy.opensource.security.domain.bo.SimpleShiroUserBO;
import org.pzy.opensource.security.service.ShiroWinterUserService;
import org.pzy.opensource.security.util.ShiroFilterUtil;
import org.pzy.opensource.web.util.HttpResponseUtl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用于校验当前用户是否拥有访问该uri的权限,并拥有跨域支持!
 *
 * @author pan
 * @date 2020-01-28
 */
@Slf4j
public class CrossUriUserPermissionFilter extends UserFilter implements WinterShiroFilter {

    private String filterName;

    private ShiroWinterUserService shiroWinterUserService;

    /**
     * 发现用户无访问当前uri的权限之后,转入的url
     */
    private String forbiddenRedirectUrl;

    /**
     * 构造方法
     *
     * @param shiroWinterUserService 用于查询用户权限
     * @param forbiddenRedirectUrl   发现用户无访问当前uri的权限之后,转入的url
     */
    public CrossUriUserPermissionFilter(ShiroWinterUserService shiroWinterUserService, String forbiddenRedirectUrl) {
        this.filterName = "crossUriUserAuthc";
        this.shiroWinterUserService = shiroWinterUserService;
        this.forbiddenRedirectUrl = forbiddenRedirectUrl;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isOptionOrLogin = ShiroFilterUtil.requestIsOptionsMethod(request) || super.pathsMatch(super.getLoginUrl(), httpServletRequest.getRequestURI());
        if (log.isDebugEnabled()) {
            log.debug("当前请求方式为:[{}], uri地址:[{}], 是否是OPTIONS请求或登录请求:{}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), isOptionOrLogin);
        }
        if (isOptionOrLogin) {
            return true;
        } else {
            String method = httpServletRequest.getMethod();
            String requestUri = httpServletRequest.getRequestURI();
            Subject subject = getSubject(request, response);
            // 当前用户已登录或包含记住我信息或当前是登录请求
            SimpleShiroUserBO shiroUserBO = (SimpleShiroUserBO) subject.getPrincipal();
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
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String getDesc() {
        return "用于校验当前用户是否拥有访问该uri的权限,并拥有跨域支持!";
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
