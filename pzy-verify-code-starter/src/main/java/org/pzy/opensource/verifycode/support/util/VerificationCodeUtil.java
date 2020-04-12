package org.pzy.opensource.verifycode.support.util;


import org.pzy.opensource.verifycode.domain.constant.VerificationCodeConstant;
import org.pzy.opensource.verifycode.domain.enums.CheckCodeVerifyStatusEnums;
import org.pzy.opensource.verifycode.domain.enums.VerificationCodeErrorEnum;
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
     * @return
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
     * @return
     */
    public static boolean isPassedVerifyCodeCheck(HttpServletRequest httpServletRequest) {
        Object checkStatus = httpServletRequest.getAttribute(VerificationCodeConstant.CHECK_CODE_VERIFY_STATUS);
        if (null == checkStatus) {
            return false;
        }
        return CheckCodeVerifyStatusEnums.VERIFY_PASS.toString().equalsIgnoreCase(checkStatus.toString());
    }

    /**
     * 判断验证码错误类型是否为客户端id错误
     *
     * @param httpServletRequest 请求对象
     * @return
     */
    public static boolean verifyCodeErrorResonIsClientIdError(HttpServletRequest httpServletRequest) {
        Object val = getParamValueByName(httpServletRequest, VerificationCodeConstant.VERIFY_CODE_ERROR_TYPE);
        if (null == val) {
            return false;
        }
        return !VerificationCodeErrorEnum.VERIFY_CODE_ERROR.toString().equalsIgnoreCase(val.toString());
    }

    /**
     * 判断验证码错误类型是否为用户输入的验证码不正确
     *
     * @param httpServletRequest 请求对象
     * @return
     */
    public static boolean verifyCodeErrorResonIsCodeError(HttpServletRequest httpServletRequest) {
        Object val = getParamValueByName(httpServletRequest, VerificationCodeConstant.VERIFY_CODE_ERROR_TYPE);
        if (null == val) {
            return false;
        }
        return VerificationCodeErrorEnum.VERIFY_CODE_ERROR.toString().equalsIgnoreCase(val.toString());
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
