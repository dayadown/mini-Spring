package com.my.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.my.beans.BeansException;
import com.my.beans.PropertyValue;
import com.my.beans.factory.config.BeanDefinition;


/**
 * 有自动装配能力的抽象bean容器，实现了抽象bean容器的创建bean的方法，没有实获取bean信息的方法，所以还是抽象类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
	//使用构造哈函数策略创建实例
	private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
		return doCreateBean(beanName, beanDefinition);
	}

	/**
	 * 创建bean实例，并且放入单例池，体现了“自动装配”
	 * @param beanName
	 * @param beanDefinition
	 * @return
	 */
	protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
		Class beanClass = beanDefinition.getBeanClass();
		Object bean = null;
		try {
			//调用createBeanInstance(bean实例策略)创建实例
			//bean=createBeanInstance(beanDefinition);
			bean=instantiationStrategy.instantiate(beanDefinition);
			//为bean填充属性
			applyPropertyValues(beanName, bean, beanDefinition);
		} catch (Exception e) {
			throw new BeansException("Instantiation of bean failed", e);
		}

		addSingleton(beanName, bean);
		return bean;
	}

	/**
	 * 根据bean信息为bean填充属性
	 * @param beanName
	 * @param bean
	 * @param beanDefinition
	 */
	protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
		try {
			for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
				String name = propertyValue.getName();
				Object value = propertyValue.getValue();

				//BeanUtil.setFieldValue 方法会反射地访问bean的字段并设置其值
				BeanUtil.setFieldValue(bean, name, value);
			}
		} catch (Exception ex) {
			throw new BeansException("Error setting property values for bean: " + beanName, ex);
		}
	}

	/**
	 * 根据bean信息创建实例
	 * @param beanDefinition
	 * @return
	 */
	protected Object createBeanInstance(BeanDefinition beanDefinition) {
		return getInstantiationStrategy().instantiate(beanDefinition);
	}

	/**
	 * instantiationStrategy的get和set方法
	 * @return
	 */
	public InstantiationStrategy getInstantiationStrategy() {
		return instantiationStrategy;
	}

	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}
}
