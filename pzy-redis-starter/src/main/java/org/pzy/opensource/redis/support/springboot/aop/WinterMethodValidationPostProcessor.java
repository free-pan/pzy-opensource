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
 * 自定义`MethodValidationPostProcessor`类, 覆盖`postProcessAfterInitialization`解决`MethodValidationInterceptor`执行优先级问题
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
        // 如果是AOP相关的基础组件bean，如ProxyProcessorSupport类及其子类，则直接返回
        if (this.advisor == null || bean instanceof AopInfrastructureBean) {
            // Ignore AOP infrastructure such as scoped proxies.
            return bean;
        }

        if (bean instanceof Advised) {
            // 如果已经是Advised的，即已经是被动态代理的实例，则直接添加advisor
            Advised advised = (Advised) bean;
            if (!advised.isFrozen() && isEligible(AopUtils.getTargetClass(bean))) {
                // 如果没有被frozen(即冷冻，不再做改动的动态代理实例)且是Eligbile(合适的)，则把其添加到advisor中。根据配置决定插入位置
                // Add our local Advisor to the existing proxy's Advisor chain...
                if (this.beforeExistingAdvisors) {
                    advised.addAdvisor(0, this.advisor);
                } else {
                    // 获取已有切面
                    Advisor[] advisorArr = advised.getAdvisors();
                    // 遍历已有切面与当前切面的order值比较, 找到第一个比当前切面order值大的切面, 并记下该位置
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
                        // 在第一个比当前切面order值大位置插入当前切面, 其它切面一次往后移一位
                        advised.addAdvisor(curAdvisorPos, this.advisor);
                    }
                }
                return bean;
            }
        }

        if (isEligible(bean, beanName)) {
            // 如果是Eligible合适的，且还不是被代理的类，则创建一个代理类的实例并返回
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
