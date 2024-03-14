package com.my.beans.factory.support;

import com.my.beans.BeansException;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.BeanPostProcessor;
import com.my.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象bean容器，唯一具有容器性质的是单例池，该抽象容器是单例池的“升级”封装
 * 真正具有容器性质的是单例池DefaultSingletonBeanRegistry，所以该类继承了DefaultSingletonBeanRegistry
 * 而bean容器不仅有单例池，getBean()在用户看来是简单的从bean容器里拿到bean，但是该bean容器内部（单例池）可能并不存在所要
 * 的bean，而是临时创建根据bean信息创建再返回，所以对于这个bean看似bean在容器中，实则不在，这就为什么叫做“抽象容器”
 *
 *
 * 拥有bean实例化后处理队列
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

	//bean后处理对象队列，需要依次执行
	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


	//实现BeanFactory的getBean方法，其中调用的是DefaultSingletonBeanRegistry的获取单例bean的方法
	//若单例池中没有，则调用getBeanDefinition获取bean的信息创建bean再返回
	@Override
	public Object getBean(String name) throws BeansException {
		Object bean = getSingleton(name);
		if (bean != null) {
			return bean;
		}

		BeanDefinition beanDefinition = getBeanDefinition(name);
		return createBean(name, beanDefinition);
	}


	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return ((T) getBean(name));
	}

	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		//有则覆盖
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}
}
