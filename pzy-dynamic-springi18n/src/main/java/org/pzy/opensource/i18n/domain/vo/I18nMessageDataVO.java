package org.pzy.opensource.i18n.domain.vo;

import lombok.Data;

/**
 * I18n消息数据
 *
 * @author pan
 * @since 2020/9/26
 */
@Data
public class I18nMessageDataVO {
    /**
     * 区域语言编码
     */
    private String locale;
    /**
     * 消息编码
     */
    private String code;
    /**
     * 消息内容
     */
    private String txt;
}
