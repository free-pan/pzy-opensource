package org.pzy.opensource.redis.support.springboot.aop;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author pan
 * @date 2020/3/30
 */
public class WinterMethodValidationPostProcessor extends MethodValidationPostProcessor {
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        DefaultPointcutAdvisor defaultPointcutAdvisor = (DefaultPointcutAdvisor) this.advisor;
        defaultPointcutAdvisor.setOrder(this.getOrder());
    }
}
