package com.my.beans.factory.config;

import com.my.beans.PropertyValues;

/**
 * 保存bean信息，保存了bean的Class信息和属性信息
 */
public class BeanDefinition {

	//Class信息
	private Class beanClass;

	//属性信息
	private PropertyValues propertyValues;

	public BeanDefinition(Class beanClass) {
		this.beanClass = beanClass;
		this.propertyValues=null;
	}

	/**
	 * 带属性列表的构造函数
	 * @param beanClass
	 * @param propertyValues
	 */
	public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
		this.beanClass = beanClass;
		this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
	}

	public Class getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class beanClass) {
		this.beanClass = beanClass;
	}

	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}
}
