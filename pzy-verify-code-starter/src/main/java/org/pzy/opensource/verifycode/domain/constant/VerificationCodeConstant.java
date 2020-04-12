package org.pzy.opensource.verifycode.domain.constant;

/**
 * @author pan
 * @date 2019-06-21
 */
public interface VerificationCodeConstant {

    /**
     * 图片验证码redis的key前缀
     */
    String VERIFICATION_CODE_REDIS_KEY_PREFIX = "verification_code:";

    String CLIENT_ID = "verification_code_client_id";
    String VERIFICATION_CODE_ID = "pic_verification_code";
    String CHECK_CODE_VERIFY_STATUS = "check_code_verify_status";
    String VERIFY_CODE_ERROR_TYPE = "verifyCodeErrorType";

    /**
     * 图片验证码过期时间. 300秒(5分钟)
     */
    Long EXPIRE_SECONDS = 300L;

}
