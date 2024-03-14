package com.my.test.ioc.common;

import com.my.beans.BeansException;
import com.my.beans.PropertyValue;
import com.my.beans.PropertyValues;
import com.my.beans.factory.ConfigurableListableBeanFactory;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.BeanFactoryPostProcessor;

/**
 * bean信息后处理
 */
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		BeanDefinition personBeanDefiniton = beanFactory.getBeanDefinition("person");
		PropertyValues propertyValues = personBeanDefiniton.getPropertyValues();
		//将person的name属性改为ivy
		propertyValues.addPropertyValue(new PropertyValue("name", "ivy"));
	}
}
