package org.pzy.opensource.redis.support.springboot.aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.core.Ordered;
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

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (this.advisor == null || bean instanceof AopInfrastructureBean) {
            // Ignore AOP infrastructure such as scoped proxies.
            return bean;
        }

        if (bean instanceof Advised) {
            Advised advised = (Advised) bean;
            if (!advised.isFrozen() && isEligible(AopUtils.getTargetClass(bean))) {
                // Add our local Advisor to the existing proxy's Advisor chain...
                if (this.beforeExistingAdvisors) {
                    advised.addAdvisor(0, this.advisor);
                } else {
                    Advisor[] advisorArr = advised.getAdvisors();
                    Integer curAdvisorPos = null;
                    for (int i = 0; i < advisorArr.length; i++) {
                        Advisor tmp = advisorArr[i];
                        if (tmp instanceof Ordered) {
                            int tmpOrder = ((Ordered) tmp).getOrder();
                            if (tmpOrder > this.getOrder()) {
                                // 当前拦截器的执行优先级高于数组中当前循环的这个优先级, 所以在这个位置插入当前拦截器
                                curAdvisorPos = i;
                                break;
                            }
                        }
                    }
                    if (null == curAdvisorPos) {
                        advised.addAdvisor(this.advisor);
                    } else {
                        advised.addAdvisor(curAdvisorPos, this.advisor);
                    }
                }
                return bean;
            }
        }

        if (isEligible(bean, beanName)) {
            ProxyFactory proxyFactory = prepareProxyFactory(bean, beanName);
            if (!proxyFactory.isProxyTargetClass()) {
                evaluateProxyInterfaces(bean.getClass(), proxyFactory);
            }
            proxyFactory.addAdvisor(this.advisor);
            customizeProxyFactory(proxyFactory);
            return proxyFactory.getProxy(getProxyClassLoader());
        }

        // No proxy needed.
        return bean;
    }
}
