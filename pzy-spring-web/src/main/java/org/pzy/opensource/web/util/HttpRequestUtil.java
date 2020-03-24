package org.pzy.opensource.web.util;

import org.apache.commons.lang3.StringUtils;
import org.pzy.opensource.web.domain.bo.HttpRequestUriInfoBO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest/HttpServletResponse操作帮助类
 *
 * @author pan
 * @date 2019-04-10
 */
public class HttpRequestUtil {

    private HttpRequestUtil() {
    }

    private static final String JSON = "JSON";
    private static final String XML = "XML";

    public static HttpRequestUriInfoBO extractHttpRequestUriInfo(HttpServletRequest request) {
        return new HttpRequestUriInfoBO(request.getMethod(), request.getRequestURI());
    }

    /**
     * 响应数据格式是否为json
     *
     * @param request 请求对象
     * @return true是, false否
     */
    public static boolean isResponseJsonData(HttpServletRequest request) {
        String accept = extractAccept(request);
        if (StringUtils.isEmpty(accept)) {
            return false;
        }
        return accept.toUpperCase().contains(JSON);
    }

    /**
     * 响应数据格式是否为xml
     *
     * @param request 请求对象
     * @return true是, false否
     */
    public static boolean isResponseXmlData(HttpServletRequest request) {
        String accept = extractAccept(request);
        if (StringUtils.isEmpty(accept)) {
            return false;
        }
        return accept.toUpperCase().contains(XML);
    }

    /**
     * 获取响应数据格式
     *
     * @param request 请求对象
     * @return 数据格式
     */
    public static String extractAccept(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (StringUtils.isEmpty(accept)) {
            accept = request.getHeader("accept");
        }
        if (StringUtils.isEmpty(accept)) {
            accept = request.getHeader("ACCEPT");
        }
        return accept;
    }

    /**
     * 获取请求头的Origin
     *
     * @param request 请求对象
     * @return 请求头的origin
     */
    public static String extractOrigin(HttpServletRequest request) {
        String accept = request.getHeader("Origin");
        if (StringUtils.isEmpty(accept)) {
            accept = request.getHeader("origin");
        }
        if (StringUtils.isEmpty(accept)) {
            accept = request.getHeader("ORIGIN");
        }
        return accept;
    }

    /**
     * 通过spring获取HttpServletRequest对象
     *
     * 前提条件: 需要配置监听器 `org.springframework.web.context.request.RequestContextListener`
     *
     * @return HttpServletRequest对象
     */
    public static HttpServletRequest loadHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取访问用户的客户端IP(适用于公网与局域网)
     *
     * @param request 请求对象
     * @return 访问用户的客户端ip
     */
    public String getIpAddr(HttpServletRequest request) {
        return IpUtil.getIpAddr(request);
    }
}
