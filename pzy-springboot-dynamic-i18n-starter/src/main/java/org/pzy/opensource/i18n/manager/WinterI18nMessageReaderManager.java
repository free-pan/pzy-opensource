package org.pzy.opensource.i18n.manager;

import org.pzy.opensource.i18n.domain.bo.LocaleAndCodeBO;
import org.pzy.opensource.i18n.domain.vo.I18nMessageDataVO;

/**
 * 读取国际化资源操作类
 *
 * @author pan
 * @since 2020/9/26
 */
public interface WinterI18nMessageReaderManager {

    /**
     * 根据区域语言标识和国际化编码查找
     *
     * @param localeAndCodeBO
     * @return
     */
    I18nMessageDataVO searchByLocaleAndCode(LocaleAndCodeBO localeAndCodeBO);
}
