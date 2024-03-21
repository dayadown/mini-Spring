package com.my.aop.framework;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;


/**
 * 反射方法调用，用反射执行方法原来的执行逻辑
 * 方法拦截器的invoke函数需要传入MethodInvocation，所以这里对原对象的各类资源做包装，以适配拦截器函数
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

	protected final Object target;

	protected final Method method;
	protected final Object[] arguments;

	public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
		this.target = target;
		this.method = method;
		this.arguments = arguments;
	}

	/**
	 * 执行方法
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object proceed() throws Throwable {
		return method.invoke(target, arguments);
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public Object getThis() {
		return target;
	}

	@Override
	public AccessibleObject getStaticPart() {
		return method;
	}
}
