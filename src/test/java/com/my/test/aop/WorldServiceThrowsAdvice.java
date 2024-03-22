package com.my.test.aop;

import com.my.aop.framework.advice.MethodThrowsAdvice;

import java.lang.reflect.Method;

public class WorldServiceThrowsAdvice implements MethodThrowsAdvice {
    @Override
    public void whileThrows(Method method, Object[] args, Object target) {
        System.out.println("异常通知");
    }
}
