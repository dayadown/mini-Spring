package com.my.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.my.beans.BeansException;
import com.my.beans.PropertyValue;
import com.my.beans.factory.*;
import com.my.beans.factory.config.AutowireCapableBeanFactory;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.BeanPostProcessor;
import com.my.beans.factory.config.BeanReference;
import lombok.Data;

import java.lang.reflect.Method;


/**
 * 有自动装配能力的抽象bean容器，实现了抽象bean容器的创建bean的方法，没有实获取bean信息的方法，所以还是抽象类
 */
@Data
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

		//往单例池中加bean，加的时候将有销毁方法的bean加到单独的池中，而所有bean放入另一池中
		//也就是将有销毁方法的bean复制一份封装起来（有定义其销毁方法）放在单独的池中
		registerDisposableBeanIfNecessary(beanName,bean,beanDefinition);
		addSingleton(beanName, bean);
		return bean;
	}

	/**
	 * 注册有销毁方法的bean，即bean继承自DisposableBean或有自定义的销毁方法
	 *
	 * @param beanName
	 * @param bean
	 * @param beanDefinition
	 */
	protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
		//如果该bean实现了DisposableBean销毁接口或者bean信息中含有销毁方法的方法名，即用户自定义了销毁方法
		if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
			//将该bean用适配器封装
			registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
		}
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
		//如果该bean实现了BeanFactoryAware，即该bean可以感知到beanFactory的存在，
		// 将this（即BeanFactory实例对象）暴露给其setBeanFactory方法
		if (bean instanceof BeanFactoryAware) {
			((BeanFactoryAware) bean).setBeanFactory(this);
		}

		//执行BeanPostProcessor的前置处理
		Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

		//执行bean的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Throwable e) {
			throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
        }

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
	 * 执行bean的初始化方法（两个，由实现接口InitializingBean重写的初始化方法和自定义但是在xml中配置的初始化方法）
	 *
	 * @param beanName
	 * @param bean
	 * @param beanDefinition
	 * @throws Throwable
	 */
	protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Throwable {
		//第一部分
		//如果该bean实现了InitializingBean接口，那么会自动执行该接口的实现方法
		if (bean instanceof InitializingBean) {
			((InitializingBean) bean).afterPropertiesSet();
		}
		//第二部分，从bean信息中获取初始化方法的名字
		String initMethodName = beanDefinition.getInitMethodName();
		//该名字不为空（且该类不能同时实现了接口的同时又配置了与接口方法相同的自定义方法，否则会执行两次）
		if (StrUtil.isNotEmpty(initMethodName) && !(bean instanceof InitializingBean && "afterPropertiesSet".equals(initMethodName))) {
			//反射获取该方法
			Method initMethod = ClassUtil.getPublicMethod(beanDefinition.getBeanClass(), initMethodName);
			if (initMethod == null) {
				throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
			}
			//反射执行该方法，该方法时在该bean中定义
			initMethod.invoke(bean);
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
}
