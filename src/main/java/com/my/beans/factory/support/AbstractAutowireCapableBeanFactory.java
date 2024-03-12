package com.my.beans.factory.support;

import com.my.beans.BeansException;
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
			//调用createBeanInstance创建实例
			//bean=createBeanInstance(beanDefinition);
			bean=instantiationStrategy.instantiate(beanDefinition);
		} catch (Exception e) {
			throw new BeansException("Instantiation of bean failed", e);
		}

		addSingleton(beanName, bean);
		return bean;
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
