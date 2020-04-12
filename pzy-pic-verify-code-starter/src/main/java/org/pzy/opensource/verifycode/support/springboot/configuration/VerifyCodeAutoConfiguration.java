package org.pzy.opensource.verifycode.support.springboot.configuration;

import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.verifycode.controller.VerificationCodeController;
import org.pzy.opensource.verifycode.support.springboot.filter.VerificationCodeFilter;
import org.pzy.opensource.verifycode.support.springboot.properties.VerifyCodeConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

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

    public VerifyCodeAutoConfiguration() {
        if (log.isDebugEnabled()) {
            log.debug("图片验证码启用!");
        }
    }

    @ConditionalOnProperty(name = "component.verify-code.enable", havingValue = "true")
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
