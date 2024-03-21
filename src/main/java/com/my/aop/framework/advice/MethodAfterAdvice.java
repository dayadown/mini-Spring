package com.my.aop.framework.advice;

import java.lang.reflect.Method;

//后置通知方法
public interface MethodAfterAdvice extends AfterAdvice{
    public void after(Method method, Object[] args, Object target);
}
