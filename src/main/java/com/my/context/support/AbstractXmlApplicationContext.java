package com.my.context.support;

import com.my.beans.factory.support.DefaultListableBeanFactory;
import com.my.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 通过资源加载器从配置文件加载bean信息
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
		//两个参数：1.资源注册器（即beanFactory本身） 2.资源加载器，自身继承了资源加载器
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			//资源读取器读取资源中的bean信息到beanFactory的bean信息容器中
			beanDefinitionReader.loadBeanDefinitions(configLocations);
		}
	}

	/**
	 * 获取资源地址
	 * @return
	 */
	protected abstract String[] getConfigLocations();
}
