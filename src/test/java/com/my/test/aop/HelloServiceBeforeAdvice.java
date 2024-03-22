package com.my.test.aop;

import com.my.aop.framework.advice.MethodBeforeAdvice;

import java.lang.reflect.Method;


public class HelloServiceBeforeAdvice implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		System.out.println("前置通知");
	}
}
