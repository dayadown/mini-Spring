package com.my.beans.factory.config;

import com.my.beans.BeansException;
import com.my.beans.factory.ConfigurableListableBeanFactory;


/**
 * bean容器后处理接口
 * 在实例化bean之前修改bean信息
 */
public interface BeanFactoryPostProcessor {

	/**
	 * 在所有BeanDefintion加载完成后，但在bean实例化之前，提供修改BeanDefinition属性值的机制
	 *
	 * @param beanFactory
	 * @throws BeansException
	 */
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
