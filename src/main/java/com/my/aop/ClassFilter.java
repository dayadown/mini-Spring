package com.my.aop;


/**
 * 类选择器（选择即看是否有该类）
 */
public interface ClassFilter {

	/**
	 * 能否找到某一个类
	 * @param clazz
	 * @return
	 */
	boolean matches(Class<?> clazz);
}
