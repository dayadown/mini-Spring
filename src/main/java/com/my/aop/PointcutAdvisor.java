package com.my.aop;


/**
 * 带通知的切点
 */
public interface PointcutAdvisor extends Advisor {

	Pointcut getPointcut();
}
