package com.my.aop;


/**
 * 切点抽象，在一个切点下，可以去选择类或者方法
 * 切点表达式会指定一个路径范围，该路径范围即是一个切点
 */
public interface Pointcut {

	ClassFilter getClassFilter();

	MethodMatcher getMethodMatcher();
}
