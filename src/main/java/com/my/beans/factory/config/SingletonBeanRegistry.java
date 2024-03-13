package com.my.beans.factory.config;


/**
 * 单例池注册功能接口
 */
public interface SingletonBeanRegistry {

	void addSingleton(String beanName,Object singletonObject);
}
