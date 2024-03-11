package com.my.beans.factory.support;

import com.my.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例池类，实现单例bean注册接口SingletonBeanRegistry
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

	//单例池
	private Map<String, Object> singletonObjects = new HashMap<>();

	//@Override
	protected Object getSingleton(String beanName) {
		return singletonObjects.get(beanName);
	}

	@Override
	public void addSingleton(String beanName, Object singletonObject) {
		singletonObjects.put(beanName, singletonObject);
	}
}
