package org.pzy.opensource.verifycode.domain.enums;

/**
 * 验证码的验证状态
 *
 * @author pan
 * @date 2019-07-30
 */
public enum CheckCodeVerifyStatusEnums {
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
    VERIFY_FAIL
}
