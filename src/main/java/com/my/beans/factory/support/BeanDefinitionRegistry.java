package com.my.beans.factory.support;

import com.my.beans.BeansException;
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

	/**
	 * 根据名称查找BeanDefinition
	 *
	 * @param beanName
	 * @return
	 * @throws BeansException 如果找不到BeanDefinition
	 */
	BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	/**
	 * 是否包含指定名称的BeanDefinition
	 *
	 * @param beanName
	 * @return
	 */
	boolean containsBeanDefinition(String beanName);

	/**
	 * 返回定义的所有bean的名称
	 *
	 * @return
	 */
	String[] getBeanDefinitionNames();
}
