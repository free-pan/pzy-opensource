package org.pzy.opensource.web.util;

import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.OS;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 获取网络客户端信息帮助类
 *
 * @author pan
 * @date 4/11/20
 */
public class HttpClientInfoUtil {

    private HttpClientInfoUtil() {
    }

    /**
     * 获取完整客户端信息
     *
     * @return 客户端完整信息
     */
    public static final Optional<UserAgent> getUserAgent() {
        String userAgentString = HttpRequestUtil.loadHttpServletRequest().getHeader("User-Agent");
        if (StringUtils.isEmpty(userAgentString)) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(UserAgentUtil.parse(userAgentString));
        }
    }

    /**
     * 获取客户端的操作系统
     *
     * @return
     */
    public static final String getClientOS() {
        Optional<UserAgent> userAgent = getUserAgent();
        if (userAgent.isPresent()) {
            return userAgent.get().getOs().getName();
        } else {
            return OS.NameUnknown;
        }
    }

    /**
     * 获取客户端浏览器信息
     *
     * @return
     */
    public static final String getClientBrowser() {
        Optional<UserAgent> userAgent = getUserAgent();
        StringBuilder stringBuilder = new StringBuilder();
        if (userAgent.isPresent()) {
            UserAgent agent = userAgent.get();
            // 获取浏览器信息
            if (null != agent.getBrowser()) {
                stringBuilder.append(agent.getBrowser().getName());
            }
            // 获取浏览器引擎信息
            if (null != agent.getEngine()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append(agent.getEngine().getName());
            }
            // 获取浏览器引擎版本
            if (!StringUtils.isEmpty(agent.getEngineVersion())) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append(agent.getEngineVersion());
            }
        } else {
            stringBuilder.append(Browser.NameUnknown);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取客户端ip
     *
     * @return 客户端ip
     */
    public static final String getClientIp() {
        return IpUtil.getIpAddr(HttpRequestUtil.loadHttpServletRequest());
    }
}
