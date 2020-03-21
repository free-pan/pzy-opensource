package org.pzy.opensource.web.util;

import javax.servlet.http.Cookie;

/**
 * @author pan
 * @since 2020-02-13
 */
public class SpringWebCookieUtil {

    private SpringWebCookieUtil() {
    }

    /**
     * 根据名称获取cookie值
     *
     * @param name cookie名称
     * @return 存在时, 返回cookie值, 不存在时,返回null
     */
    public static final String getValue(String name) {
        return CookieUtil.getValue(HttpRequestUtil.loadHttpServletRequest(), name);
    }

    /**
     * 删除cookie
     *
     * @param name cookie名称
     */
    public static final void remove(String name) {
        CookieUtil.remove(HttpResponseUtl.loadHttpServletResponse(), name);
    }

    /**
     * 根据名称获取cookie
     *
     * @param name cookie名称
     * @return 存在时, 返回cookie对象, 不存在时,返回null
     */
    public static final Cookie get(String name) {
        return CookieUtil.get(HttpRequestUtil.loadHttpServletRequest(), name);
    }

    /**
     * 设置cookie
     *
     * @param name     名称
     * @param value    值
     * @param httpOnly 为true时,js无法读写该cookie
     * @param expiry   过期时间. 单位:秒. -1表示浏览器关闭时,自动清除该cookie
     */
    public static final void add(String name, String value, boolean httpOnly, int expiry) {
        CookieUtil.add(HttpResponseUtl.loadHttpServletResponse(), name, value, httpOnly, expiry);
    }

    /**
     * 设置超时时间为-1的cookie
     *
     * @param name     名称
     * @param value    值
     * @param httpOnly 为true时,js无法读写该cookie
     */
    public static final void addSessionCookie(String name, String value, boolean httpOnly) {
        CookieUtil.addSessionCookie(HttpResponseUtl.loadHttpServletResponse(), name, value, httpOnly);
    }

    /**
     * 清除所有cookie
     */
    public static void clearAll() {
        CookieUtil.clearAll(HttpRequestUtil.loadHttpServletRequest(), HttpResponseUtl.loadHttpServletResponse());
    }
}
