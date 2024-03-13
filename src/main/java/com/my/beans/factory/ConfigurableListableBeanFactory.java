package com.my.beans.factory;

import com.my.beans.BeansException;
import com.my.beans.factory.config.AutowireCapableBeanFactory;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.ConfigurableBeanFactory;

/**
 * 能实现列表操作，能自动装配，可配置的bean容器。+可根据名称查找bean信息
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

	/**
	 * 根据名称查找BeanDefinition
	 *
	 * @param beanName
	 * @return
	 * @throws BeansException 如果找不到BeanDefinition
	 */
	BeanDefinition getBeanDefinition(String beanName) throws BeansException;

}
