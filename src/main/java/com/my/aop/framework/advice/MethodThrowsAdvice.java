package com.my.aop.framework.advice;

import java.lang.reflect.Method;

public interface MethodThrowsAdvice extends ThrowsAdvice{
    public void whileThrows(Method method, Object[] args, Object target);
}
