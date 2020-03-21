package org.pzy.opensource.web.util;

import org.pzy.opensource.domain.GlobalConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * cookie读写帮助类
 *
 * @author pan
 * @since 2020-02-07
 */
public class CookieUtil {

    private CookieUtil() {
    }

    /**
     * 根据名称获取cookie值
     *
     * @param request
     * @param name    cookie名称
     * @return 存在时, 返回cookie值, 不存在时,返回null
     */
    public static final String getValue(HttpServletRequest request, String name) {
        Cookie cookie = get(request, name);
        if (null != cookie) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param name     cookie名称
     */
    public static final void remove(HttpServletResponse response, String name) {
        add(response, name, null, false, GlobalConstant.ZERO.intValue());
    }

    /**
     * 根据名称获取cookie
     *
     * @param request
     * @param name    cookie名称
     * @return
     */
    public static final Cookie get(HttpServletRequest request, String name) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * 设置cookie
     *
     * @param response
     * @param name     名称
     * @param value    值
     * @param httpOnly 为true时,js无法读写该cookie
     * @param expiry   过期时间. 单位:秒. -1表示浏览器关闭时,自动清除该cookie
     */
    public static final void add(HttpServletResponse response, String name, String value, boolean httpOnly, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }

    /**
     * 设置超时时间为-1的cookie
     *
     * @param response
     * @param name     名称
     * @param value    值
     * @param httpOnly 为true时,js无法读写该cookie
     */
    public static final void addSessionCookie(HttpServletResponse response, String name, String value, boolean httpOnly) {
        add(response, name, value, httpOnly, -1);
    }

    /**
     * 清除所有cookie
     *
     * @param request
     * @param response
     */
    public static void clearAll(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            Arrays.stream(cookies).forEach(cookie -> {
                remove(response, cookie.getName());
            });
        }
    }
}
