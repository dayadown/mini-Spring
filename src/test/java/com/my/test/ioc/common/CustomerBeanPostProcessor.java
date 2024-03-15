package com.my.test.ioc.common;

import com.my.beans.BeansException;
import com.my.beans.factory.config.BeanPostProcessor;
import com.my.test.ioc.Car;

/**
 * bean实例后处理
 */
public class CustomerBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println(beanName+"初始化前置方法执行了");
		//换兰博基尼
		if ("car".equals(beanName)) {
			((Car) bean).setBrand("lamborghini");
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println(beanName+"初始化后置方法执行了");
		return bean;
	}
}
