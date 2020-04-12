package org.pzy.opensource.verifycode.domain.enums;

/**
 * @author pan
 * @date 2019-07-30
 */
public enum VerificationCodeErrorEnum {
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
    VERIFY_CODE_ERROR
}
