package org.pzy.opensource.security.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.util.StringUtils;
import org.pzy.opensource.security.domain.bo.PermissionInfoBO;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author pan
 * @date 2020-01-28
 */
public class ShiroFilterUtil {

    private static final PatternMatcher PATH_MATCHER = new AntPathMatcher();

    private ShiroFilterUtil() {
    }

    private static final String ALL_METHOD = "ALL";
    private static final String OPTION_METHOD = "OPTIONS";


    /**
     * 验证是否为option请求
     *
     * @param request
     * @return
     */
    public static boolean requestIsOptionsMethod(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String method = httpServletRequest.getMethod();
        return isOptionsMethod(method);
    }

    /**
     * 判断是否为OPTIONS方法
     *
     * @param method
     * @return
     */
    public static boolean isOptionsMethod(String method) {
        if (OPTION_METHOD.equalsIgnoreCase(method)) {
            // 对于OPTION请求直接放行
            return true;
        }
        return false;
    }

    /**
     * 判断是否有权限
     *
     * @param permissionInfoList
     * @param requestMethod
     * @param requestUri
     * @return
     */
    public static boolean hasPermission(List<PermissionInfoBO> permissionInfoList, String requestMethod, String requestUri) {
        // 遍历权限信息,进行鉴权
        if (!CollectionUtils.isEmpty(permissionInfoList)) {
            for (PermissionInfoBO permissionInfoBO : permissionInfoList) {
                // 找到与当前uri匹配的权限信息
                String tmpMethod = permissionInfoBO.getRequestMethod();
                if (hasSameUri(requestUri, permissionInfoBO) && hasSameRequestMethod(requestMethod, tmpMethod)) {
                    // 当前uri与权限信息的路径表达式匹配,且请求方式也匹配,则认为用户拥有权限
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前请求的uri与权限信息中的ant路劲表达式是否匹配
     *
     * @param uri
     * @param permissionInfoBO
     * @return
     */
    public static boolean hasSameUri(String uri, PermissionInfoBO permissionInfoBO) {
        return StringUtils.hasText(permissionInfoBO.getPathPattern()) && PATH_MATCHER.matches(permissionInfoBO.getPathPattern(), uri);
    }

    /**
     * 判断当前请求的请求方式与权限信息中的请求方式是否匹配
     *
     * @param requestMethod               当前请求的实际请求方式
     * @param permissionInfoRequestMethod 权限信息中存储的请求方式
     * @return
     */
    public static boolean hasSameRequestMethod(String requestMethod, String permissionInfoRequestMethod) {
        return requestMethod.equalsIgnoreCase(permissionInfoRequestMethod) || ALL_METHOD.equalsIgnoreCase(permissionInfoRequestMethod);
    }
}
