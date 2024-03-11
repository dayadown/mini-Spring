package com.my.beans.factory.support;

import com.my.beans.factory.config.BeanDefinition;

/**
 * bean信息注册接口
 */
public interface BeanDefinitionRegistry {

	/**
	 * 向注册表中注BeanDefinition
	 *
	 * @param beanName
	 * @param beanDefinition
	 */
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
