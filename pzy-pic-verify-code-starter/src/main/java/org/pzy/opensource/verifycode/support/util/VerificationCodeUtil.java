package org.pzy.opensource.verifycode.support.util;


import org.pzy.opensource.verifycode.domain.constant.VerificationCodeConstant;
import org.pzy.opensource.verifycode.domain.enums.CheckCodeVerifyStatusEnums;
import org.pzy.opensource.verifycode.domain.enums.VerifyCodeValidateFailTypeEnum;
import org.pzy.opensource.verifycode.support.picture.Randoms;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pan
 * @date 2019-06-21
 */
public class VerificationCodeUtil {

    private VerificationCodeUtil() {
    }

    /**
     * 构建保存图片验证码的redis的key
     *
     * @param clientId 客户端id
     * @return 返回key
     */
    public static String buildVerificationCodeRedisKey(String clientId) {
        return VerificationCodeConstant.VERIFICATION_CODE_REDIS_KEY_PREFIX + clientId;
    }

    /**
     * 生成随机验证码
     *
     * @param len 验证码长度
     * @return 生成的验证码
     */
    public static String alphas(int len) {
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            cs[i] = Randoms.alpha();
        }
        return new String(cs);
    }

    /**
     * 判断当前请求是否有通过验证码过滤器的验证
     *
     * @param httpServletRequest 请求对象
     * @return 当前请求是否已通过验证码过滤器的验证. true表示验证码校验通过, false表示验证码校验未通过
     */
    public static CheckCodeVerifyStatusEnums loadCheckCodeVerifyStatus(HttpServletRequest httpServletRequest) {
        Object checkStatus = httpServletRequest.getAttribute(VerificationCodeConstant.CHECK_CODE_VERIFY_STATUS);
        if (null == checkStatus) {
            return CheckCodeVerifyStatusEnums.NOT_EXECUTE_VERIFY;
        }
        return CheckCodeVerifyStatusEnums.string2Enum(checkStatus.toString());
    }

    /**
     * 获取验证码验证失败的失败类型
     *
     * @param httpServletRequest 请求对象
     * @return 验证码验证失败的失败类型
     */
    public static VerifyCodeValidateFailTypeEnum loadVerifyCodeValidateFailType(HttpServletRequest httpServletRequest) {
        Object val = getParamValueByName(httpServletRequest, VerificationCodeConstant.VERIFY_CODE_ERROR_TYPE);
        if (null == val) {
            return VerifyCodeValidateFailTypeEnum.VERIFY_CODE_ERROR;
        }
        return VerifyCodeValidateFailTypeEnum.string2Enum(val.toString());
    }

    /**
     * 根据请求参数名获取参数值
     *
     * @param httpServletRequest 请求对象
     * @param paramName          参数名
     * @return 参数值
     */
    private static Object getParamValueByName(HttpServletRequest httpServletRequest, String paramName) {
        Object val = httpServletRequest.getAttribute(paramName);
        if (null == val) {
            val = httpServletRequest.getParameter(paramName);
        }
        return val;
    }
}
