package com.my.beans.factory.support;

import com.my.beans.BeansException;
import com.my.core.io.DefaultResourceLoader;
import com.my.core.io.ResourceLoader;
import lombok.Getter;
import lombok.Setter;

/**
 * bean信息读取器类，实现bean信息读取器接口，
 * 有加载资源并读取资源中的bean信息到bean容器的能力
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	//定义bean信息注册器
	@Getter
	private final BeanDefinitionRegistry registry;

	//定义资源加载器
	@Getter
	@Setter
	private ResourceLoader resourceLoader;



	//必须要有BeanDefinitionRegistry  bean信息注册器，因为它是一个抽象类，无法当场创建实例
	protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
		this(registry, new DefaultResourceLoader());
	}

	public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
		this.registry = registry;
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void loadBeanDefinitions(String[] locations) throws BeansException {
		for (String location : locations) {
			loadBeanDefinitions(location);
		}
	}
}
