package com.my.beans.factory.support;

import com.my.beans.BeansException;
import com.my.beans.factory.DisposableBean;
import com.my.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例池类，实现单例bean注册接口SingletonBeanRegistry
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

	//单例池
	private Map<String, Object> singletonObjects = new HashMap<>();

	//有销毁方法的单例池，进入该池中的类必须实现DisposableBean接口
	private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

	//@Override
	protected Object getSingleton(String beanName) {
		return singletonObjects.get(beanName);
	}

	@Override
	public void addSingleton(String beanName, Object singletonObject) {
		singletonObjects.put(beanName, singletonObject);
	}

	/**
	 * 注册有销毁方法的类
	 * @param beanName
	 * @param bean
	 */
	public void registerDisposableBean(String beanName, DisposableBean bean) {
		disposableBeans.put(beanName, bean);
	}

	/**
	 * 销毁有销毁方法的bean
	 */
	public void destroySingletons() {
		//获取所有有销毁方法的bean的名字
		ArrayList<String> beanNames = new ArrayList<>(disposableBeans.keySet());
		for (String beanName : beanNames) {
			//销毁一个便从map中移除一个
			DisposableBean disposableBean = disposableBeans.remove(beanName);
			try {
				//调用适配器的销毁方法，适配器销毁方法中按照两个销毁方法（实现接口的，用户自定义的）的执行次序执行
				disposableBean.destroy();
			} catch (Exception e) {
				throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
			}
		}
	}
}
