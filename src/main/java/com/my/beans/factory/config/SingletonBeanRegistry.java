package com.my.beans.factory.config;


/**
 * 单例bean的注册接口
 */
public interface SingletonBeanRegistry {

	void addSingleton(String beanName,Object singletonObject);
}
