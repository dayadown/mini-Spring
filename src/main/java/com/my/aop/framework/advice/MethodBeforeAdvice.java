package com.my.aop.framework.advice;

import java.lang.reflect.Method;

/**
 * 前置通知方法，需要在前置通知中实现的逻辑即before函数
 */
public interface MethodBeforeAdvice extends BeforeAdvice {
    void before(Method method, Object[] args, Object target) throws Throwable;
}