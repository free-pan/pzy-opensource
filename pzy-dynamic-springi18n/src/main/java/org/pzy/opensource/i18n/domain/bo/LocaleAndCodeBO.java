package org.pzy.opensource.i18n.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 区域语言标识和国际化消息编码
 *
 * @author pan
 * @since 2020/9/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocaleAndCodeBO {
    /**
     * 区域语言标识
     */
    private String locale;
    /**
     * 国际化编码
     */
    private String code;
}
