package org.pzy.opensource.domain.enums;

import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.domain.entity.BaseEnum;

/**
 * 日期格式字符串枚举
 *
 * @author pan
 * @date 2020/4/5 16:55
 */
public enum LocalDatePatternEnum implements BaseEnum<String> {

    /**
     * 日期模式字符串: yyyy-MM-dd
     */
    DATE_PATTERN(GlobalConstant.DATE_PATTERN),
    /**
     * 日期时间字符串: yyyy-MM-dd 00:00:00
     */
    DATE_TIME_PATTERN_E(GlobalConstant.DATE_TIME_PATTERN_E),
    /**
     * 日期时间字符串: yyyy-MM-dd 23:59:59
     */
    DATE_TIME_PATTERN_F(GlobalConstant.DATE_TIME_PATTERN_F),
    /**
     * 日期时间字符串: yyyy-MM-dd 00:00
     */
    DATE_TIME_PATTERN_G(GlobalConstant.DATE_TIME_PATTERN_G),
    /**
     * 日期时间字符串: yyyy-MM-dd 23:59
     */
    DATE_TIME_PATTERN_H(GlobalConstant.DATE_TIME_PATTERN_H);

    /**
     * 日期格式
     */
    private String code;

    LocalDatePatternEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
}
