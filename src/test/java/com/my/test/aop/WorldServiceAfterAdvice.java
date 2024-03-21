package com.my.test.aop;


import com.my.aop.framework.advice.MethodAfterAdvice;

import java.lang.reflect.Method;

public class WorldServiceAfterAdvice implements MethodAfterAdvice {
    @Override
    public void after(Method method, Object[] args, Object target) {
        System.out.println("后置通知");
    }
}
