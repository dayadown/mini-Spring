package com.my.beans.factory.support;

import com.my.beans.BeansException;
import com.my.beans.factory.config.BeanDefinition;


/**
 * bean实例化策略接口，通过继承该接口实现不同的实例化策略
 */
public interface InstantiationStrategy {

	/**
	 * 该方法需要通过bean信息实例化bean
	 * @param beanDefinition
	 * @return
	 * @throws BeansException
	 */
	Object instantiate(BeanDefinition beanDefinition) throws BeansException;
}
