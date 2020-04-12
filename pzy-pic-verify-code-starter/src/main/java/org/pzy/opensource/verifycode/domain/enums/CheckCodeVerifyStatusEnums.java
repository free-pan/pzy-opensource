package org.pzy.opensource.verifycode.domain.enums;

import org.pzy.opensource.domain.entity.BaseEnum;

/**
 * 验证码的验证状态
 *
 * @author pan
 * @date 2019-07-30
 */
public enum CheckCodeVerifyStatusEnums implements BaseEnum<String> {
    /**
     * 未执行验证
     */
    NOT_EXECUTE_VERIFY,
    /**
     * 验证通过
     */
    VERIFY_PASS,
    /**
     * 验证不通过
     */
    VERIFY_FAIL;

    /**
     * 字符串转枚举
     *
     * @param checkCodeVerifyStatus 验证状态字符串
     * @return 当验证状态字符串为null或空时, 返回NOT_EXECUTE_VERIFY, 否则返回匹配到的枚举
     */
    public static CheckCodeVerifyStatusEnums string2Enum(String checkCodeVerifyStatus) {
        for (CheckCodeVerifyStatusEnums e : CheckCodeVerifyStatusEnums.values()) {
            if (e.name().equals(checkCodeVerifyStatus)) {
                return e;
            }
        }
        return CheckCodeVerifyStatusEnums.NOT_EXECUTE_VERIFY;
    }
}
