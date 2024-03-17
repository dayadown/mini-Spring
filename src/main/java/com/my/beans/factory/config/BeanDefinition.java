package com.my.beans.factory.config;

import com.my.beans.PropertyValues;
import lombok.Data;

/**
 * 保存bean信息，保存了bean的Class信息和属性信息
 */
@Data
public class BeanDefinition {
	//Class信息
	private Class beanClass;

	//属性信息
	private PropertyValues propertyValues;

	//bean构造方法的名字
	private String initMethodName;

	//bean销毁方法的名字
	private String destroyMethodName;

	//bean的创建模式，scope
	//单例
	public static String SCOPE_SINGLETON = "singleton";
	//原型
	public static String SCOPE_PROTOTYPE = "prototype";
	//默认是单例的
	private String scope = SCOPE_SINGLETON;
	private boolean singleton = true;
	private boolean prototype = false;

	public BeanDefinition(Class beanClass) {
		this(beanClass, null);
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

}
