package com.my.aop;

import java.lang.reflect.Method;

/**
 * 方法选择器
 */
public interface MethodMatcher {

	/**
	 * 能否找到某类的的某方法
	 * @param method
	 * @return
	 */
	boolean matches(Method method);
}
