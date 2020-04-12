package org.pzy.opensource.verifycode.support.springboot.configuration;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.verifycode.controller.VerificationCodeController;
import org.pzy.opensource.verifycode.domain.constant.VerificationCodeConstant;
import org.pzy.opensource.verifycode.support.springboot.filter.VerificationCodeFilter;
import org.pzy.opensource.verifycode.support.springboot.properties.VerifyCodeConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * @author pan
 * @date 2019-06-22
 */
@Slf4j
@EnableConfigurationProperties({VerifyCodeConfigProperties.class})
@ConditionalOnWebApplication
public class VerifyCodeAutoConfiguration {

    @Autowired
    private VerifyCodeConfigProperties verifyCodeConfigProperties;

    @PostConstruct
    public void init() {
        if (log.isDebugEnabled()) {
            log.debug("图片验证码以及过滤器启用!");
            log.debug("验证码的有效期为:[{}]秒, 会对这些地址执行验证码校验:{}, 验证码验证不通过时会转入:[{}]", verifyCodeConfigProperties.getExpiresSeconds(), JsonUtil.toJsonString(verifyCodeConfigProperties.getFilterUrls()), verifyCodeConfigProperties.getErrorRedirectUrl());
            log.debug("1. 生成图片验证码之前需要先通过VerificationCodeController类下的接口获取客户端id.");
            log.debug("2. 需要进行图片验证码验证的请求地址需要配置到[component.pic-verify-code.filterUrls]中.这样VerificationCodeFilter才会对该请求进行验证验证.");
            log.debug("3. 无论生成验证码图片还是进行验证码验证都需要携带第一步中获取到客户端id(放入请求头中).客户端id的参数名为:[{}]", VerificationCodeConstant.CLIENT_ID);
            log.debug("4. 客户端输入的验证码放入请求头中, 验证码的参数名为:[{}]", VerificationCodeConstant.VERIFICATION_CODE_ID);
            log.debug("4. 请使用 VerificationCodeUtil.loadCheckCodeVerifyStatus(), VerificationCodeUtil.loadVerifyCodeValidateFailType() 帮助类中的方法获取验证码过滤器的实时执行状态, 以及验证码过滤器执行结果!");
        }
    }

    @ConditionalOnProperty(name = "component.pic-verify-code.enable", havingValue = "true")
    @Bean
    public FilterRegistrationBean picVerifyCodeFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new VerificationCodeFilter(verifyCodeConfigProperties));
        //拦截规则
        if (null == verifyCodeConfigProperties.getFilterUrls() || verifyCodeConfigProperties.getFilterUrls().length == 0) {
            throw new RuntimeException("验证码过滤器设置错误, 请设置需要过滤的url或停用验证码过滤器!");
        }
        registration.addUrlPatterns(verifyCodeConfigProperties.getFilterUrls());
        // 设置过滤器名称
        registration.setName("picVerifyCodeFilter");
        // 设置过滤器顺序
        registration.setOrder(15);
        log.debug("验证码过滤器启用, 过滤器的执行优先级为15, 过滤的url为: " + JsonUtil.toPrettyJsonString(verifyCodeConfigProperties.getFilterUrls()));
        log.info("请勿忘记将 `org.pzy.opensource.verifycode.controller` 加入swagger的包扫描路径");
        return registration;

    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication
    VerificationCodeController verificationCodeController() {
        log.debug("VerificationCodeController注册");
        return new VerificationCodeController();
    }

}
