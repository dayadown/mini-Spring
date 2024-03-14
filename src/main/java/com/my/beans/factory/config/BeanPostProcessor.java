package com.my.beans.factory.config;

import com.my.beans.BeansException;

/**
 * bean后处理接口
 * 在bean根据bean信息实例化后进行，对bean做修改或替换
 * 可在bean初始化（即属性填充等）前或后执行
 */
public interface BeanPostProcessor {

	/**
	 * 在bean执行初始化方法之前执行此方法
	 *
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

	/**
	 * 在bean执行初始化方法之后执行此方法
	 *
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
