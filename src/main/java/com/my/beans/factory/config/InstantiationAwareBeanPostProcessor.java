package com.my.beans.factory.config;

import com.my.beans.BeansException;

/**
 * 一个特殊的BeanPostProcessor，在bean实例化之前加入BeanPostProcessor池
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * 在bean实例化之前执行
	 *
	 * @param beanClass
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;
}
