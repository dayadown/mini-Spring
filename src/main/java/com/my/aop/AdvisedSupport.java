package com.my.aop;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;


/**
 * 代理支持，包含代理时需要的各类对象或参数
 * 代理的对象，方法拦截器，方法选择器
 */
@Getter
@Setter
public class AdvisedSupport {

	private TargetSource targetSource;

	private MethodInterceptor methodInterceptor;

	private MethodMatcher methodMatcher;
}
