package org.pzy.opensource.mybatisplus.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * spring帮助类, 用于进行事件发布或者bean的获取
 *
 * @author pan
 * @date 2020/3/28
 */
@Slf4j
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;

    private static final SpringUtil INSTANCE = new SpringUtil();

    private SpringUtil() {
    }

    public static SpringUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 根据bean类型获取bean
     *
     * @param beanType bean类型
     * @param <T>
     * @return bean实例
     */
    public static <T> T getBean(Class<T> beanType) {
        return APPLICATION_CONTEXT.getBean(beanType);
    }

    /**
     * 根据bean名称和类型获取bean
     *
     * @param name     bean名称
     * @param beanType bean类型
     * @param <T>
     * @return bean实例
     */
    public static <T> T getBean(String name, Class<T> beanType) {
        return APPLICATION_CONTEXT.getBean(name, beanType);
    }

    /**
     * 根据bean名称获取bean
     *
     * @param name bean名称
     * @return bean实例
     */
    public static Object getBean(String name) {
        return APPLICATION_CONTEXT.getBean(name);
    }

    /**
     * 如果当前上线文存在事务则在当前事务提交完毕之后进行事件发布, 否则立刻进行事件发布
     * <p>事件发布/监听机制会将一些次要逻辑从主逻辑中抽取出来. 通常会结合异步任务使用.
     * 但此时要注意如果主逻辑中的事务如果提交比较慢, 可能会导致异步任务中, 无法从数据库中获取到主事务中的数据的问题. 该方法解决了这个问题
     * <p>详细参考: https://www.jyoryo.com/archives/155.html
     *
     * @param applicationEvent
     */
    public static void publishEventOnAfterCommitIfNecessary(ApplicationEvent applicationEvent) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 当前上线文存在活动的事务
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    if (log.isDebugEnabled()) {
                        log.debug("当前上下文存在事务, 事务[" + TransactionSynchronizationManager.getCurrentTransactionName() + "]提交之后进行事件发布:" + applicationEvent);
                    }
                    APPLICATION_CONTEXT.publishEvent(applicationEvent);
                }
            });
        } else {
            // 当前上下文不存在活动的事务
            if (log.isDebugEnabled()) {
                log.debug("当前上下文不存在事务, 直接进行事件发布:" + applicationEvent);
            }
            APPLICATION_CONTEXT.publishEvent(applicationEvent);
        }
    }
}
