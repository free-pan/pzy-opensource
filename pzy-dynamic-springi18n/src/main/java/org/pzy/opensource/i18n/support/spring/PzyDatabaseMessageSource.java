package org.pzy.opensource.i18n.support.spring;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.i18n.dao.PzyI18nMessageDao;
import org.pzy.opensource.i18n.domain.bo.LocaleAndCodeBO;
import org.pzy.opensource.i18n.domain.vo.I18nMessageDataVO;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将数据库作为国际化资源数据源
 *
 * @author pan
 * @since 2020/9/26
 */
@Primary
@Slf4j
public class PzyDatabaseMessageSource extends AbstractMessageSource implements ResourceLoaderAware {

    /**
     * 这个是用来缓存数据库中获取到的配置的 数据库配置更改的时候可以调用reload方法重新加载
     */
    private static final Map<String, Map<String, String>> LOCAL_CACHE = new ConcurrentHashMap<>(256);

    private ResourceLoader resourceLoader;

    private PzyI18nMessageDao pzyI18nMessageDao;

    public PzyDatabaseMessageSource(PzyI18nMessageDao pzyI18nMessageDao) {
        this.pzyI18nMessageDao = pzyI18nMessageDao;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader == null ? new DefaultResourceLoader() : resourceLoader);
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        // 查数据库获取国际化资源
        I18nMessageDataVO i18nMessageDataVO = this.pzyI18nMessageDao.findByLocaleAndCode(new LocaleAndCodeBO(locale.getLanguage(), code));
        String i18nMessage = null;
        if (null != i18nMessageDataVO) {
            i18nMessage = i18nMessageDataVO.getTxt();
        } else {
            log.error("通过区域语言标识[{}]和国际化编码[{}],未找到对应国际化资源", locale.getLanguage(), code);
            if (null != super.getParentMessageSource()) {
                // 通过上级MessageSource获取国际化资源
                i18nMessage = super.getParentMessageSource().getMessage(code, null, locale);
            }
        }
        if (null != i18nMessage) {
            return new MessageFormat(i18nMessage, locale);
        } else {
            return null;
        }
    }
}
