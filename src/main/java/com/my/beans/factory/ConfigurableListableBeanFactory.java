package com.my.beans.factory;

import com.my.beans.BeansException;
import com.my.beans.factory.config.AutowireCapableBeanFactory;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.ConfigurableBeanFactory;

/**
 * 能实现列表操作，能自动装配，可配置的bean容器。+可以获得bean信息
 * 增加可根据名称获得bean信息的功能，这样就可以更改bean信息，
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

	/**
	 * 根据名称获得BeanDefinition
	 *
	 * @param beanName
	 * @return
	 * @throws BeansException 如果找不到BeanDefinition
	 */
	BeanDefinition getBeanDefinition(String beanName) throws BeansException;



}
