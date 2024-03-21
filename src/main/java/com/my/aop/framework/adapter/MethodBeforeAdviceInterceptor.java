package com.my.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import com.my.aop.framework.advice.MethodBeforeAdvice;


/**
 *前置通知的方法拦截器，执行前置通知方法后再执行原方法的逻辑
 * 将前置通知适配到拦截器的逻辑之中
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

	//声明前置通知
	private MethodBeforeAdvice advice;

	public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
		this.advice = advice;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//在执行被代理方法之前，先执行前置通知方法
		this.advice.before(invocation.getMethod(), invocation.getArguments(), invocation.getThis());
		return invocation.proceed();
	}
}
