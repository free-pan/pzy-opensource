package org.pzy.opensource.verifycode.domain.enums;

import org.pzy.opensource.domain.entity.BaseEnum;

/**
 * 验证码验证失败的类型
 *
 * @author pan
 * @date 2019-07-30
 */
public enum VerifyCodeValidateFailTypeEnum implements BaseEnum<String> {
    /**
     * 客户端id过期
     */
    CLIENT_ID_EXPIRE,
    /**
     * 客户端id不存在或失效或无效
     */
    CLIENT_ID_INVALID,
    /**
     * 用户输入的验证码错误
     */
    VERIFY_CODE_ERROR,
    /**
     * 验证码已过期
     */
    VERIFY_CODE_EXPIRE,
    /**
     * 未知原因
     */
    UNKNOWN;

    /**
     * 字符串转枚举
     *
     * @param validateFailType 验证失败类型字符串
     * @return 验证失败类型枚举. 如果字符串未与枚举的name匹配, 则返回UNKNOWN
     */
    public static VerifyCodeValidateFailTypeEnum string2Enum(String validateFailType) {
        for (VerifyCodeValidateFailTypeEnum e : VerifyCodeValidateFailTypeEnum.values()) {
            if (e.name().equals(validateFailType)) {
                return e;
            }
        }
        return UNKNOWN;
    }
}
