package com.my.beans.factory.support;

import com.my.beans.BeansException;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.BeanPostProcessor;
import com.my.beans.factory.config.ConfigurableBeanFactory;
import com.my.beans.factory.FactoryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	//存放factoryBean内部的getObject返回值
	private final Map<String, Object> factoryBeanObjectCache = new HashMap<>();


	//实现BeanFactory的getBean方法，其中调用的是DefaultSingletonBeanRegistry的获取单例bean的方法
	//若单例池中没有，则调用getBeanDefinition获取bean的信息创建bean再返回
	@Override
	public Object getBean(String name) throws BeansException {
		Object sharedInstance = getSingleton(name);
		if (sharedInstance != null) {
			//如果是FactoryBean，从FactoryBean#getObject中创建bean
			return getObjectForBeanInstance(sharedInstance, name);
		}

		BeanDefinition beanDefinition = getBeanDefinition(name);
		Object bean = createBean(name, beanDefinition);
		return getObjectForBeanInstance(bean, name);
	}

	/**
	 * 判断该bean是否为FactoryBean，如果是FactoryBean，从FactoryBean#getObject中创建bean
	 * 不是的话就直接返回
	 *
	 * @param beanInstance
	 * @param beanName
	 * @return
	 */
	protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
		Object object = beanInstance;
		/*if (beanInstance instanceof FactoryBean) {
			FactoryBean factoryBean = (FactoryBean) beanInstance;
			try {
				if (factoryBean.isSingleton()) {
					//singleton作用域bean，从缓存中获取
					object = this.factoryBeanObjectCache.get(beanName);
					if (object == null) {
						object = factoryBean.getObject();
						this.factoryBeanObjectCache.put(beanName, object);
					}
				} else {
					//prototype作用域bean，新创建bean
					object = factoryBean.getObject();
				}
			} catch (Exception ex) {
				throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", ex);
			}
		}*/
		//如果该bean是FactoryBean
		if (beanInstance instanceof FactoryBean) {
			FactoryBean factoryBean = (FactoryBean) beanInstance;
			//从单例池查
			object = this.factoryBeanObjectCache.get(beanName);
			if(object==null){
			//没查到，调用bean内部方法创建
                try {
                    object = factoryBean.getObject();
					//判断是否为内部单例，如果是要放入池中，否则直接返回
					if(factoryBean.isSingleton()){
						this.factoryBeanObjectCache.put(beanName, object);
					}
                } catch (Exception e) {
					throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
                }
            }
		}
		return object;
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
