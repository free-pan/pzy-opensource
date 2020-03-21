/*
 * Copyright (c) [2019] [潘志勇]
 *    [pzy-opensource] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 */

package org.pzy.opensource.redis.support.springboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.pzy.opensource.comm.exception.SystemBusyException;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.redis.support.springboot.annotation.LockBuilder;
import org.pzy.opensource.redis.support.springboot.annotation.WinterLock;
import org.pzy.opensource.redis.support.springboot.annotation.WinterLocks;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 锁切面
 *
 * @author pzy
 * @date 2019/1/10
 */
@Aspect
@Order(GlobalConstant.AOP_ORDER_LOCK) //值越大,执行优先级越低
@Slf4j
public class WinterLockAop {

    /**
     * 锁大分类名. 所有使用注解方式加的锁, 都以这个为前缀
     */
    private static final String LOCK_TYPE_NAME = "WinterLock:";
    private RedissonClient redissonClient;

    public WinterLockAop(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        log.debug("WinterLockAop切面注册,在方法上加上 `@WinterLock` 注解即可为方法自动 加/解 Redis分布式锁!");
    }

    private static ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    @Pointcut(value = "@annotation(org.pzy.opensource.redis.support.springboot.annotation.WinterLock) || @annotation(org.pzy.opensource.redis.support.springboot.annotation.WinterLocks)")
    private void getPointcut() {

    }

    private ExecuteMethodInfo buildExecuteMethodInfo(ProceedingJoinPoint point) {
        // 类名
        String targetName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] arguments = point.getArgs();
        int paramCount = 0;
        if (null != arguments) {
            paramCount = arguments.length;
        }
        return new ExecuteMethodInfo(point, arguments, targetName, methodName, paramCount);
    }

    @Around("getPointcut()")
    public Object aroundAopMethod(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        WinterLock winterLock = method.getAnnotation(WinterLock.class);
        WinterLocks winterLocks = method.getAnnotation(WinterLocks.class);
        Object target = null;
        if (null != winterLock) {
            ExecuteMethodInfo executeMethodInfo = buildExecuteMethodInfo(point);
            printLog(executeMethodInfo, "WinterLockAop在{}方法上执行!");
            boolean conditionResultIsTrue = evalCondition(winterLock.lockBuilder().condition(), executeMethodInfo.getArguments());
            if (conditionResultIsTrue) {
                // 该方法需要加锁
                target = autoLockAndExecute(winterLock, executeMethodInfo);
            } else {
                // 该方法无需加锁
                printLog(executeMethodInfo, "WinterLockAop在{}方法上执行!,但condition结果为false,因此未启用锁!");
                target = point.proceed();
            }
        } else if (null != winterLocks) {
            ExecuteMethodInfo executeMethodInfo = buildExecuteMethodInfo(point);
            printLog(executeMethodInfo, "WinterLockAop在{}方法上执行!");
            LockBuilder[] buildArr = winterLocks.lockBuilder();
            List<RLock> lockList = new ArrayList<>();
            for (LockBuilder lockBuilder : buildArr) {
                boolean conditionResultIsTrue = evalCondition(lockBuilder.condition(), executeMethodInfo.getArguments());
                if (conditionResultIsTrue) {
                    List<RLock> tmpLockList = buildRLockList(lockBuilder, executeMethodInfo);
                    lockList.addAll(tmpLockList);
                }
            }
            RLock lock = null;
            if (lockList.size() == 1) {
                try {
                    lock = lockList.get(0);
                    target = tryLockAndExecute(lock, winterLocks, executeMethodInfo);
                } finally {
                    if (null != lock) {
                        lock.unlock();
                    }
                }
            } else if (lockList.size() > 1) {
                try {
                    lock = buildRedissonMultiLock(lockList);
                    target = tryLockAndExecute(lock, winterLocks, executeMethodInfo);
                } finally {
                    if (null != lock) {
                        lock.unlock();
                    }
                }
            } else {
                // 该方法无需加锁
                target = point.proceed();
            }
        }
        return target;
    }

    private void printLog(ExecuteMethodInfo executeMethodInfo, String s) {
        if (log.isDebugEnabled()) {
            log.debug(s, executeMethodInfo.getTargetName() + ":" + executeMethodInfo.getMethodName() + "#" + executeMethodInfo.getParamCount());
        }
    }

    private RLock buildRedissonMultiLock(List<RLock> lockList) {
        RLock lock;
        RLock[] lockArr = new RLock[lockList.size()];
        lockList.toArray(lockArr);
        lock = new RedissonMultiLock(lockArr);
        return lock;
    }

    /**
     * 给方法加锁,执行, 获取返回结果, 再解锁
     *
     * @param winterLock
     * @param executeMethodInfo
     * @return
     * @throws Throwable
     */
    private Object autoLockAndExecute(WinterLock winterLock, ExecuteMethodInfo executeMethodInfo) throws Throwable {
        Object target;

        // 获取自定义的lockKey
        List<RLock> lockList = buildRLockList(winterLock.lockBuilder(), executeMethodInfo);
        // 是否需要使用高级方法锁
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        RLock lock = null;
        try {
            if (!CollectionUtils.isEmpty(lockList)) {
                log.debug("实际需要锁,正在获取锁...");
                // 实际上需要获取锁
                if (lockList.size() == 1) {
                    // 获取普通可重入锁
                    lock = lockList.get(0);
                    target = tryLockAndExecute(lock, winterLock, executeMethodInfo);
                } else {
                    // 获取连锁
                    lock = buildRedissonMultiLock(lockList);
                    target = tryLockAndExecute(lock, winterLock, executeMethodInfo);
                }
            } else {
                log.debug("实际无需获取锁!");
                // 实际上无需获取锁
                target = executeMethodInfo.getPoint().proceed();
            }
        } catch (InterruptedException e) {
            String errorMsg = "执行" + executeMethodInfo.getTargetName() + "." + executeMethodInfo.getMethodName() + "#" + executeMethodInfo.getParamCount() + "方法需要加锁,但未获取到锁! 租期(单位:秒)=" + winterLock.leaseTime() + " ,waitTime=" + winterLock.waitTime();
            throw new SystemBusyException("系统繁忙请稍后重试!", new RuntimeException(errorMsg, e));
        } finally {
            stopWatch.stop();
            if (null != lock) {
                // 释放锁
                lock.unlock();
            }
        }
        return target;
    }

    private List<RLock> buildRLockList(LockBuilder lockBuilder, ExecuteMethodInfo executeMethodInfo) {
        HashSet<String> lockKeySet = buildLockKey(lockBuilder, executeMethodInfo);
        List<RLock> lockList = new ArrayList<>(lockKeySet.size());
        for (String lockKey : lockKeySet) {
            lockList.add(redissonClient.getLock(lockKey));
        }
        return lockList;
    }

    private Object tryLockAndExecute(RLock lock, WinterLock winterLock, ExecuteMethodInfo executeMethodInfo) throws Throwable {
        boolean hasLock = lock.tryLock(winterLock.waitTime(), winterLock.leaseTime(), TimeUnit.SECONDS);
        return executeProceed(executeMethodInfo, hasLock);
    }

    private Object tryLockAndExecute(RLock lock, WinterLocks winterLocks, ExecuteMethodInfo executeMethodInfo) throws Throwable {
        boolean hasLock = lock.tryLock(winterLocks.waitTime(), winterLocks.leaseTime(), TimeUnit.SECONDS);
        return executeProceed(executeMethodInfo, hasLock);
    }

    private Object executeProceed(ExecuteMethodInfo executeMethodInfo, boolean hasLock) throws Throwable {
        if (hasLock) {
            log.debug("实际需要锁,且锁获取成功.");
            // 成功获取锁
            return executeMethodInfo.getPoint().proceed();
        } else {
            log.warn("实际需要锁,但锁获取失败!");
            // 未获取到锁
            // 抛出未获取到锁异常
            String errorMsg = "执行" + executeMethodInfo.getTargetName() + "." + executeMethodInfo.getMethodName() + "#" + executeMethodInfo.getParamCount() + "方法需要加锁,但未获取到锁!";
            throw new SystemBusyException("系统繁忙请稍后重试!", new RuntimeException(errorMsg));
        }
    }


    private HashSet<String> buildLockKey(LockBuilder lockBuilder, ExecuteMethodInfo executeMethodInfo) {
        HashSet<String> keySet = new HashSet<>();
        String springEL = lockBuilder.key();
        if (StringUtils.isEmpty(springEL)) {
            keySet = new HashSet<>(1);
            // 未自定义lockKey,按默认规则生成
            keySet.add(LOCK_TYPE_NAME + lockBuilder.prefix() + executeMethodInfo.getTargetName() + ":" + executeMethodInfo.getMethodName() + "#" + executeMethodInfo.getParamCount());
        } else if (lockBuilder.keyEl()) {
            // 按照自定义规则生成lockKey
            HashSet<String> tmpSet = evalLockKey(springEL, executeMethodInfo.getArguments());
            if (!CollectionUtils.isEmpty(tmpSet)) {
                for (String t : tmpSet) {
                    keySet.add(LOCK_TYPE_NAME + lockBuilder.prefix() + ":" + t);
                }
            }
        }
        return keySet;
    }


    /**
     * 使用spring el计算lock key
     *
     * @param springEl  spring的el表达式
     * @param arguments 当前切面方法的参数
     * @return lock key
     */
    private HashSet<String> evalLockKey(String springEl, Object[] arguments) {
        HashSet<String> keySet = new HashSet<>();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(arguments);
        Expression expression = EXPRESSION_PARSER.parseExpression(springEl);
        Object obj = expression.getValue(context, Object.class);
        if (null != obj) {
            if (obj instanceof Collection) {
                // spring el的结果为集合类型
                Collection coll = (Collection) obj;
                for (Object tmp : coll) {
                    if (null != tmp) {
                        // 将非空元素加入 keySet中
                        keySet.add(tmp.toString());
                    }
                }
            } else {
                // spring el的结果为其它类型
                keySet.add(obj.toString());
            }
        }
        return keySet;
    }

    /**
     * 使用spring el计算 condition结果
     *
     * @param springEl  spring的el表达式
     * @param arguments 当前切面方法的参数
     * @return 如果springEl为空, 则返回true, 否则根据实际的执行结果返回
     */
    private Boolean evalCondition(String springEl, Object[] arguments) {
        if (StringUtils.isEmpty(springEl)) {
            return true;
        }
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(arguments);
        Expression expression = EXPRESSION_PARSER.parseExpression(springEl);
        return expression.getValue(context, Boolean.class);
    }
}
