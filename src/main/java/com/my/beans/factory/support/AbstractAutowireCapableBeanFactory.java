package com.my.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.my.beans.BeansException;
import com.my.beans.PropertyValue;
import com.my.beans.factory.config.AutowireCapableBeanFactory;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.BeanPostProcessor;
import com.my.beans.factory.config.BeanReference;


/**
 * 有自动装配能力的抽象bean容器，实现了抽象bean容器的创建bean的方法，没有实获取bean信息的方法，所以还是抽象类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
	//使用构造函数策略创建实例
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
			//执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
			bean = initializeBean(beanName, bean, beanDefinition);
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
				//判断属性类型是否为另一个bean（是否为BeanReference类型）
				if(value instanceof BeanReference){
					//value是另一个bean，则去取该bean
					BeanReference beanReference = (BeanReference) value;
					value = getBean(beanReference.getBeanName());
				}
				//BeanUtil.setFieldValue 方法会反射地访问bean的字段并设置其值
				BeanUtil.setFieldValue(bean, name, value);
			}
		} catch (Exception ex) {
			throw new BeansException("Error setting property values for bean: " + beanName, ex);
		}
	}

	/**
	 * bean的初始化方法和前置后置方法
	 * @param beanName
	 * @param bean
	 * @param beanDefinition
	 * @return
	 */
	protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
		//执行BeanPostProcessor的前置处理
		Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

		//TODO 后面会在此处执行bean的初始化方法
		invokeInitMethods(beanName, wrappedBean, beanDefinition);

		//执行BeanPostProcessor的后置处理
		wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
		return wrappedBean;
	}

	@Override
	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException {
		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			//执行后处理程序的before方法
			Object current = processor.postProcessBeforeInitialization(result, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}

	@Override
	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException {

		Object result = existingBean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessAfterInitialization(result, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}

	/**
	 * 执行bean的初始化方法
	 *
	 * @param beanName
	 * @param bean
	 * @param beanDefinition
	 * @throws Throwable
	 */
	protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) {
		//TODO 后面会实现
		System.out.println("执行bean[" + beanName + "]的初始化方法");
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
