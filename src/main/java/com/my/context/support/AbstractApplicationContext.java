package com.my.context.support;

import com.my.beans.BeansException;
import com.my.beans.factory.ConfigurableListableBeanFactory;
import com.my.beans.factory.config.BeanFactoryPostProcessor;
import com.my.beans.factory.config.BeanPostProcessor;
import com.my.context.ApplicationEvent;
import com.my.context.ApplicationListener;
import com.my.context.ConfigurableApplicationContext;
import com.my.context.event.ApplicationEventMulticaster;
import com.my.context.event.ContextClosedEvent;
import com.my.context.event.ContextRefreshedEvent;
import com.my.context.event.SimpleApplicationEventMulticaster;
import com.my.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * 抽象应用上下文
 * 可以获取资源加载器
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

	//事件多播器的beanName
	public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

	//事件多播器
	private ApplicationEventMulticaster applicationEventMulticaster;
	@Override
	public void refresh() throws BeansException {
		//创建BeanFactory，并加载BeanDefinition
		refreshBeanFactory();

		//获取创建好的BeanFactory
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();


		//在bean实例化之前，执行BeanFactoryPostProcessor，即执行xml中加载的所有bean信息后处理程序，修改bean信息
		invokeBeanFactoryPostProcessors(beanFactory);

		//注册ApplicationContextAwareProcessor，（实现了注册BeanPostProcessor，也是个bean后处理类）
		//该后处理在每个bean执行初始化前置方法时判断该bean是否实现了ApplicationContextAware
		//若实现了那么将this（即ApplicationContext）暴露给该bean
		//也许永远也成为不了谁......继续追，谁的光荣不是伴着眼泪..
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

		//注册BeanPostProcessors，将BeanPostProcessors信息读出并创建bean，将创建的bean加入bean后处理队列，初始化bean前后会拿出来执行
		registerBeanPostProcessors(beanFactory);

		//初始化事件多播器，（创建并放入单例池）
		initApplicationEventMulticaster();

		//提前实例化单例bean
		beanFactory.preInstantiateSingletons();

		//注册事件监听器，（事件多播器中加入事件监听器）（事件监听器在xml中配置为bean，在上一条语句已经进行了实例化并进入单例池）
		registerListeners();

		//完成刷新操作，发布事件
		finishRefresh();
	}


	/**
	 * 初始化事件发布者
	 */
	protected void initApplicationEventMulticaster() {
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
		applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
		beanFactory.addSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
	}

	/**
	 * 注册事件监听器
	 */
	protected void registerListeners() {
		Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
		for (ApplicationListener applicationListener : applicationListeners) {
			applicationEventMulticaster.addApplicationListener(applicationListener);
		}
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

	/**
	 *发布刷新完成事件
	 */
	public void finishRefresh(){
		publishEvent(new ContextRefreshedEvent(this));
	}


	/**
	 * 发布事件
	 * @param event
	 */
	@Override
	public void publishEvent(ApplicationEvent event) {
		applicationEventMulticaster.multicastEvent(event);
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
		//定义JVM关闭钩子
		Thread shutdownHook = new Thread() {
			public void run() {
				doClose();
			}
		};
		//在JVM关闭之前执行关闭钩子，也就是在bean容器被销毁之前执行钩子函数（重写的方法或是配置的方法）
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	//执行销毁方法
	protected void doClose() {
		//发布容器关闭事件
		publishEvent(new ContextClosedEvent(this));
		destroyBeans();
	}

	protected void destroyBeans() {
		getBeanFactory().destroySingletons();
	}
}

