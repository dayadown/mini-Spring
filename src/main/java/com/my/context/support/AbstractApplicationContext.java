package com.my.context.support;

import com.my.beans.BeansException;
import com.my.beans.factory.ConfigurableListableBeanFactory;
import com.my.beans.factory.config.BeanFactoryPostProcessor;
import com.my.beans.factory.config.BeanPostProcessor;
import com.my.context.ConfigurableApplicationContext;
import com.my.core.io.DefaultResourceLoader;

import java.util.Map;

/**
 * 抽象应用上下文
 * 可以获取资源加载器
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

	@Override
	public void refresh() throws BeansException {
		//创建BeanFactory，并加载BeanDefinition
		refreshBeanFactory();

		//获取创建好的BeanFactory
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();

		//在bean实例化之前，执行BeanFactoryPostProcessor，即执行xml中加载的所有bean信息后处理程序，修改bean信息
		invokeBeanFactoryPostProcessors(beanFactory);

		//注册BeanPostProcessors，将BeanPostProcessors信息读出并创建bean，将创建的bean加入bean后处理队列，初始化bean前后会拿出来执行
		registerBeanPostProcessors(beanFactory);

		//提前实例化单例bean
		beanFactory.preInstantiateSingletons();
	}

	/**
	 * 创建BeanFactory，并加载BeanDefinition
	 *
	 * @throws BeansException
	 */
	protected abstract void refreshBeanFactory() throws BeansException;

	/**
	 * 在bean实例化之前，执行BeanFactoryPostProcessor
	 *
	 * @param beanFactory
	 */
	protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
		//从bean容器中获得BeanFactoryPostProcessor类的bean
		//执行该方法时，容器中并没有BeanFactoryPostProcessors的bean，所以这里的getBeansOfType会从bean信息容器根据类型查找bean的名字，然后getBean,getBean会创建BeanFactoryPostProcessors
		Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
		//直接依次执行每一个BeanFactoryPostProcessor
		for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
			beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
			System.out.println("修改bean信息的方法执行了");
		}
	}

	/**
	 * 注册BeanPostProcessor
	 *
	 * @param beanFactory
	 */
	protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
		//从bean容器中获得BeanPostProcessor的bean对象
		Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
		//将每一个BeanPostProcessor加入beanFactory的bean后处理队列中，在初始化bean后会依次执行
		for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
			beanFactory.addBeanPostProcessor(beanPostProcessor);
		}
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return getBeanFactory().getBean(name, requiredType);
	}

	@Override
	public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
		return getBeanFactory().getBeansOfType(type);
	}

	public Object getBean(String name) throws BeansException {
		return getBeanFactory().getBean(name);
	}

	public String[] getBeanDefinitionNames() {
		return getBeanFactory().getBeanDefinitionNames();
	}

	public abstract ConfigurableListableBeanFactory getBeanFactory();

	public void registerShutdownHook() {
		Thread shutdownHook = new Thread() {
			public void run() {
				doClose();
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	protected void doClose() {
		destroyBeans();
	}

	protected void destroyBeans() {
		getBeanFactory().destroySingletons();
	}
}

