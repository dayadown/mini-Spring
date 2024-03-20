package com.my.aop;

import java.lang.reflect.Method;

/**
 * 方法选择器
 */
public interface MethodMatcher {

	/**
	 * 能否找到到某一个方法
	 * @param method
	 * @param targetClass
	 * @return
	 */
	boolean matches(Method method, Class<?> targetClass);
}
