package com.my.aop;

import lombok.Getter;

/**
 * 被代理的对象的封装对象
 */
@Getter
public class TargetSource {

	private final Object target;

	public TargetSource(Object target) {
		this.target = target;
	}

	/**
	 * 获取被代理对象实现的接口
	 * @return
	 */
	public Class<?>[] getTargetClass() {
		return this.target.getClass().getInterfaces();
	}

}
