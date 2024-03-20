package com.my.test.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * 方法拦截器
 */
public class WorldServiceInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("原方法前做一些事");
		Object result = invocation.proceed();
		System.out.println("原方法之后做一些事");
		return result;
	}
}
