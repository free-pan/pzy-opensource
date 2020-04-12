package org.pzy.opensource.verifycode.support.springboot.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.util.JwtUtil;
import org.pzy.opensource.redis.support.util.RedisUtil;
import org.pzy.opensource.verifycode.domain.constant.VerificationCodeConstant;
import org.pzy.opensource.verifycode.domain.enums.CheckCodeVerifyStatusEnums;
import org.pzy.opensource.verifycode.domain.enums.VerifyCodeValidateFailTypeEnum;
import org.pzy.opensource.verifycode.support.springboot.properties.VerifyCodeConfigProperties;
import org.pzy.opensource.verifycode.support.util.VerificationCodeUtil;
import org.pzy.opensource.web.util.WebUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>用于对验证码进行验证. 后续请求如果需要直到验证码验证状态,则直接使用`request.getAttribute(VerificationCodeConstant.CHECK_CODE_VERIFY_STATUS)`获取对应的值,看是否与`CheckCodeVerifyStatusEnums`中的匹配
 * <p>主体逻辑:
 * <p>1. 如果请求头中存在客户端id, 则进行验证码验证, 否则在url请求参数中设置check_code_verify_status=NOT_EXECUTE_VERIFY
 * <p>2. 执行验证码验证, 验证通过, 则在url请求参数中设置check_code_verify_status=VERIFY_PASS
 * <p>3. 验证不通过,则进行redirect跳转
 *
 * @author pan
 * @date 2019-06-21
 */
@Slf4j
public class VerificationCodeFilter extends OncePerRequestFilter {


    private VerifyCodeConfigProperties verifyCodeConfigProperties;

    public VerificationCodeFilter(VerifyCodeConfigProperties verifyCodeConfigProperties) {
        this.verifyCodeConfigProperties = verifyCodeConfigProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 删除验证码过滤器执行状态参数(目的是为了防止客户端传递假的状态,防止图片验证码过滤器的验证逻辑被跳过)
        request.removeAttribute(VerificationCodeConstant.CHECK_CODE_VERIFY_STATUS);
        // 将验证执行状态设置为:未执行
        request.setAttribute(VerificationCodeConstant.CHECK_CODE_VERIFY_STATUS, CheckCodeVerifyStatusEnums.NOT_EXECUTE_VERIFY.toString());
        // 从请求头中获取客户端id
        String jwt = request.getHeader(VerificationCodeConstant.CLIENT_ID);
        if (StringUtils.isEmpty(jwt)) {
            log.warn("根据配置,当前uri[{}]需要进行验证码校验,但请求头中不存在客户端id,因此,实际上未执行验证码验证.", request.getRequestURI());
            // 客户端id为空,则不进行验证码验证
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims;
        try {
            // 客户端id解码
            claims = JwtUtil.verify(verifyCodeConfigProperties.getSecret(), verifyCodeConfigProperties.getIssuer(), jwt);
        } catch (ExpiredJwtException e) {
            log.error("客户端id过期!", e);
            redirectToErrorUrl(request, response, VerifyCodeValidateFailTypeEnum.CLIENT_ID_EXPIRE);
            return;
        } catch (IncorrectClaimException e) {
            log.error("无效的客户端id!", e);
            redirectToErrorUrl(request, response, VerifyCodeValidateFailTypeEnum.CLIENT_ID_INVALID);
            return;
        } catch (MalformedJwtException e) {
            log.error("无效的客户端id!", e);
            redirectToErrorUrl(request, response, VerifyCodeValidateFailTypeEnum.CLIENT_ID_INVALID);
            return;
        }
        // 构建保存图片验证码的redis的key
        String redisPicVerifyCodeKey = VerificationCodeUtil.buildVerificationCodeRedisKey(claims.getId());
        // 获取redis中保存的验证码
        Object redisPicVerifyCodeObj = RedisUtil.get(redisPicVerifyCodeKey);
        if (null == redisPicVerifyCodeObj) {
            // redis中的验证码不存在或已失效
            log.debug("redis中的验证码不存在或已失效, 或者客户端尚未调用图片验证码生成结果, 该客户端id尚未生成实际的图片验证码!");
            redirectToErrorUrl(request, response, VerifyCodeValidateFailTypeEnum.VERIFY_CODE_EXPIRE);
            return;
        } else {
            // 从请求头中获取用户输入的验证码
            String picVerificationCode = request.getHeader(VerificationCodeConstant.VERIFICATION_CODE_ID);
            log.debug("客户端id为: " + claims.getId() + ", 客户端传入的验证码为: " + picVerificationCode + ", reids中的验证码为:" + redisPicVerifyCodeObj);
            if (redisPicVerifyCodeObj.toString().equalsIgnoreCase(picVerificationCode)) {
                // 验证码验证通过
                // 将验证执行状态设置为:验证通过
                request.setAttribute(VerificationCodeConstant.CHECK_CODE_VERIFY_STATUS, CheckCodeVerifyStatusEnums.VERIFY_PASS.toString());
                RedisUtil.remove(redisPicVerifyCodeKey);
                filterChain.doFilter(request, response);
            } else {
                // 验证码验证不通过
                log.debug("验证码错误!");
                redirectToErrorUrl(request, response, VerifyCodeValidateFailTypeEnum.VERIFY_CODE_ERROR);
                return;
            }
        }
    }

    private void redirectToErrorUrl(HttpServletRequest request, HttpServletResponse response, VerifyCodeValidateFailTypeEnum verifyCodeError) throws IOException {
        // 将验证执行状态设置为:验证失败
        request.setAttribute(VerificationCodeConstant.CHECK_CODE_VERIFY_STATUS, CheckCodeVerifyStatusEnums.VERIFY_FAIL.toString());
        String redirectUrl = verifyCodeConfigProperties.getErrorRedirectUrl() + (verifyCodeConfigProperties.getErrorRedirectUrl().contains("?") ? "&" : "?") +
                VerificationCodeConstant.VERIFY_CODE_ERROR_TYPE + "=" + verifyCodeError.toString();
        WebUtil.issueRedirect(request, response, redirectUrl);
    }

}
