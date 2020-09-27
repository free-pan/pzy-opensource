package org.pzy.opensource.i18n.support.spring;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.i18n.manager.WinterI18nMessageReaderManager;
import org.pzy.opensource.i18n.domain.bo.LocaleAndCodeBO;
import org.pzy.opensource.i18n.domain.vo.I18nMessageDataVO;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * 将数据库作为国际化资源数据源
 *
 * @author pan
 * @since 2020/9/26
 */
@Slf4j
public class WinterDynamicMessageSource extends AbstractMessageSource implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private WinterI18nMessageReaderManager winterI18NMessageReaderManager;

    public WinterDynamicMessageSource(WinterI18nMessageReaderManager winterI18NMessageReaderManager) {
        this.winterI18NMessageReaderManager = winterI18NMessageReaderManager;
        if (log.isDebugEnabled()) {
            log.debug("WinterDynamicMessageSource初始化完成,WinterI18nMessageReaderManager的实例对象为:" + winterI18NMessageReaderManager);
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader == null ? new DefaultResourceLoader() : resourceLoader);
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        // 查数据库获取国际化资源
        I18nMessageDataVO i18nMessageDataVO = this.winterI18NMessageReaderManager.searchByLocaleAndCode(new LocaleAndCodeBO(locale.toLanguageTag(), code));
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
