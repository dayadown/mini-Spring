package com.my.beans.factory.support;

import com.my.beans.BeansException;
import com.my.core.io.Resource;
import com.my.core.io.ResourceLoader;

/**
 * bean信息读取接口（这个读指从xml中读取bean信息到bean信息容器中）
 * 能获取bean信息注册器，资源加载器，并根据这俩实现“从xml中读取bean信息到bean信息容器中”的功能
 */
public interface BeanDefinitionReader {

	/**
	 * 获取bean信息注册器，将bean信息放入bean信息容器
	 * @return
	 */
	BeanDefinitionRegistry getRegistry();

	/**
	 * 获取资源加载器，用于加载资源
	 * @return
	 */
	ResourceLoader getResourceLoader();

	/**
	 * 从xml中读取bean信息到bean信息容器中
	 * @param resource
	 * @throws BeansException
	 */
	void loadBeanDefinitions(Resource resource) throws BeansException;

	void loadBeanDefinitions(String location) throws BeansException;

	void loadBeanDefinitions(String[] locations) throws BeansException;
}
