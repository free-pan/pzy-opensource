package org.pzy.opensource.verifycode.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.domain.bo.JwtStandardInfo;
import org.pzy.opensource.comm.util.JwtUtil;
import org.pzy.opensource.domain.ResultT;
import org.pzy.opensource.redis.support.util.RedisUtil;
import org.pzy.opensource.verifycode.domain.constant.VerificationCodeConstant;
import org.pzy.opensource.verifycode.support.springboot.properties.PicSize;
import org.pzy.opensource.verifycode.support.springboot.properties.VerifyCodeConfigProperties;
import org.pzy.opensource.verifycode.support.picture.Captcha;
import org.pzy.opensource.verifycode.support.picture.GifCaptcha;
import org.pzy.opensource.verifycode.support.picture.SpecCaptcha;
import org.pzy.opensource.verifycode.support.util.VerificationCodeUtil;
import org.pzy.opensource.web.util.HttpRequestUtil;
import org.pzy.opensource.web.util.HttpResponseUtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author pan
 * @date 2019-06-21
 */
@Controller
@RequestMapping("pu/pic-verify-code")
@Api(tags = "图片验证码服务")
@Slf4j
public class VerificationCodeController {

    @Autowired
    private VerifyCodeConfigProperties verifyCodeConfigProperties;

    /**
     * 生成客户端id
     */
    @ApiOperation(value = "生成临时客户端id", notes = "默认有效期:3分钟. 实际有效期请询问开发者")
    @GetMapping(value = "tmp-client-id")
    @ResponseBody
    public ResultT<String> generateClientId() throws IOException {
        JwtStandardInfo jwtStandardInfo = new JwtStandardInfo(verifyCodeConfigProperties.getIssuer(), verifyCodeConfigProperties.getExpiresSeconds());
        String jwt = JwtUtil.createJwt(verifyCodeConfigProperties.getSecret(), jwtStandardInfo, null);
        return ResultT.success(jwt);
    }

    /**
     * 生成gif图片验证码
     *
     * @param clientId 客户端唯一标识
     * @param flag     验证码宽高标识
     * @throws IOException io异常
     */
    @ApiOperation(value = "生成gif图片验证码", notes = "验证码有效期:3分钟")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", required = true, value = "客户端唯一标识", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "flag", required = false, value = "验证码宽高标识", paramType = "query", dataType = "string")
    })
    @GetMapping(value = "gif/{clientId}", produces = MediaType.IMAGE_GIF_VALUE)
    public void generateGif(@PathVariable("clientId") String clientId, @RequestParam(name = "flag", required = false) String flag) throws IOException {
        Claims claims;
        try {
            // 客户端id解码
            claims = JwtUtil.verify(verifyCodeConfigProperties.getSecret(), verifyCodeConfigProperties.getIssuer(), clientId);
        } catch (ExpiredJwtException e) {
            log.error("客户端id过期!", e);
            return;
        } catch (IncorrectClaimException e) {
            log.error("无效的客户端id!", e);
            return;
        } catch (MalformedJwtException e) {
            log.error("无效的客户端id!", e);
            return;
        }
        HttpServletResponse httpServletResponse = HttpResponseUtl.loadHttpServletResponse();
        HttpServletRequest httpServletRequest = HttpRequestUtil.loadHttpServletRequest();
        HttpResponseUtl.gifPicSettings(httpServletResponse, httpServletRequest);
        /**
         * gif格式动画验证码
         * 宽，高，位数。
         */
        int picWidth = this.verifyCodeConfigProperties.getWidth();
        int picHeight = this.verifyCodeConfigProperties.getHeight();
        if (!StringUtils.isEmpty(flag)) {
            PicSize picSize = this.verifyCodeConfigProperties.getCustomPicSize().get(flag);
            if (null != picSize) {
                picWidth = picSize.getWidth();
                picHeight = picSize.getHeight();
            } else {
                log.warn("未找到flag[{}]对应的验证码宽高配置, 因此依然使用默认的验证码宽高!", flag);
            }
        }
        Captcha captcha = new GifCaptcha(picWidth, picHeight, verifyCodeConfigProperties.getLength());
        //输出
        captcha.out(httpServletResponse.getOutputStream());
        // 验证码
        String code = captcha.text().toLowerCase();
        // 将验证码保存到redis中
        log.debug("解析到的实际client id为: " + claims.getId() + ", 存储到redis中的验证码为: " + code);
        RedisUtil.put(VerificationCodeUtil.buildVerificationCodeRedisKey(claims.getId()), code, VerificationCodeConstant.EXPIRE_SECONDS);
    }

    /**
     * 生成jpg图片验证码
     *
     * @param clientId 客户端唯一标识
     * @param flag     验证码宽高标识
     * @throws IOException io异常
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", required = true, value = "客户端唯一标识", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "flag", required = false, value = "验证码宽高标识", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "生成jpg图片验证码", notes = "验证码有效期:3分钟")
    @GetMapping(value = "jpg/{clientId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void generateJpg(@PathVariable("clientId") String clientId, @RequestParam(name = "flag", required = false) String flag) throws IOException {
        HttpServletResponse httpServletResponse = HttpResponseUtl.loadHttpServletResponse();
        HttpServletRequest httpServletRequest = HttpRequestUtil.loadHttpServletRequest();
        HttpResponseUtl.jpgPicSettings(httpServletResponse, httpServletRequest);
        /**
         * gif格式动画验证码
         * 宽，高，位数。
         */
        int picWidth = this.verifyCodeConfigProperties.getWidth();
        int picHeight = this.verifyCodeConfigProperties.getHeight();
        if (!StringUtils.isEmpty(flag)) {
            PicSize picSize = this.verifyCodeConfigProperties.getCustomPicSize().get(flag);
            if (null != picSize) {
                picWidth = picSize.getWidth();
                picHeight = picSize.getHeight();
            } else {
                log.warn("未找到flag[{}]对应的验证码宽高配置, 因此依然使用默认的验证码宽高!", flag);
            }
        }
        Captcha captcha = new SpecCaptcha(picWidth, picHeight, verifyCodeConfigProperties.getLength());
        //输出
        captcha.out(httpServletResponse.getOutputStream());
        // 验证码
        String code = captcha.text().toLowerCase();
        // 将验证码保存到redis中
        RedisUtil.put(VerificationCodeUtil.buildVerificationCodeRedisKey(clientId), code, VerificationCodeConstant.EXPIRE_SECONDS);
    }
}
