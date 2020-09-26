package org.pzy.opensource.i18n.support.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 从session中读取国际化区域语言设置
 *
 * @author pan
 * @since 2020/9/26
 */
@Slf4j
public class PzySessionLocaleResolver extends SessionLocaleResolver {

    private static final String SPLIT_CHAR = "_";
    /**
     * 从请求头中读取区域语言的键
     */
    private String localeHeaderName;

    public PzySessionLocaleResolver(String localeHeaderName) {
        this.localeHeaderName = localeHeaderName;
    }

    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        Locale defaultLocale = getDefaultLocale();
        if (defaultLocale == null) {
            String locale = request.getHeader(this.localeHeaderName);
            if (!StringUtils.isEmpty(locale)) {
                String[] arr = locale.split(SPLIT_CHAR);
                if (arr.length == 2) {
                    defaultLocale = new Locale(arr[0], arr[1]);
                }
            }
            if (log.isDebugEnabled() && defaultLocale == null) {
                log.debug("请求头中不存在区域语言标识[{}]或区域语言标识不正确!", locale);
            }
            if (null == defaultLocale) {
                defaultLocale = request.getLocale();
            }
        }
        return defaultLocale;
    }

}
