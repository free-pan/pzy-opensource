package org.pzy.opensource.i18n.support.spring;

import org.pzy.opensource.i18n.manager.WinterI18nMessageReaderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PostConstruct;

/**
 * 国际化资源动态加载自动配置类
 *
 * @author pan
 * @since 2020/9/27
 */
@Configuration
@AutoConfigureBefore(ValidationAutoConfiguration.class)
public class WinterDynamicI18nAutoConfiguration {

    @Autowired(required = false)
    private WinterI18nMessageReaderManager winterI18NMessageReaderManager;

    @PostConstruct
    public void init() {
        if (null == winterI18NMessageReaderManager) {
            throw new RuntimeException("请至少指定一个org.pzy.opensource.i18n.manager.WinterI18nMessageReaderManager实例!");
        }
    }

    @Bean
    @ConditionalOnMissingBean
    WinterDynamicMessageSource winterDynamicMessageSource() {
        return new WinterDynamicMessageSource(winterI18NMessageReaderManager);
    }

    @Bean
    @ConditionalOnMissingBean
    WinterSessionLocaleResolver winterSessionLocaleResolver() {
        return new WinterSessionLocaleResolver("i18n");
    }

    @Bean
    @Primary
    public Validator validator(@Autowired WinterDynamicMessageSource winterDynamicMessageSource) {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(winterDynamicMessageSource);
        return validator;
    }
}
