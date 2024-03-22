package com.my.aop;

import org.aopalliance.aop.Advice;

/**
 * 通知器
 */
public interface Advisor {

	Advice getAdvice();

}
