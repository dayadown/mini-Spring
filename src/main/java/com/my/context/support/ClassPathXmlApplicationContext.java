package com.my.context.support;

import com.my.beans.BeansException;

/**
 * xml文件的应用上下文
 * 其实是对DefaultListableBeanFactory的封装，不需要用户手动得加入bean后处理程序，不需要手动得在实例化之前执行bean信息后处理
 * 自动得识别xml中得配置，只要在xml中配置了BeanFactoryPostProcessor和BeanPostProcessor的bean，就可以在bean信息加载完后
 * 自动得执行bean信息后处理，在实例化bean后自动得执行bean后处理
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

	private String[] configLocations;

	/**
	 * 从xml文件加载BeanDefinition，并且自动刷新上下文
	 *
	 * @param configLocation xml配置文件
	 * @throws BeansException 应用上下文创建失败
	 */
	public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
		this(new String[]{configLocation});
	}

	/**
	 * 从xml文件加载BeanDefinition，并且自动刷新上下文
	 *
	 * @param configLocations xml配置文件
	 * @throws BeansException 应用上下文创建失败
	 */
	public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
		this.configLocations = configLocations;
		refresh();
	}

	protected String[] getConfigLocations() {
		return this.configLocations;
	}
}
