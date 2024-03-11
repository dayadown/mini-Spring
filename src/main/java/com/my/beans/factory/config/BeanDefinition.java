package com.my.beans.factory.config;

/**
 * 保存bean信息，简化了，只保存了bean的Class信息
 */
public class BeanDefinition {

	private Class beanClass;

	public BeanDefinition(Class beanClass) {
		this.beanClass = beanClass;
	}

	public Class getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}
}
