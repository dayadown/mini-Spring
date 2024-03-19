package com.my.context.event;

import com.my.beans.BeansException;
import com.my.beans.factory.BeanFactory;
import com.my.beans.factory.BeanFactoryAware;
import com.my.context.ApplicationEvent;
import com.my.context.ApplicationListener;

import java.util.HashSet;
import java.util.Set;

/**
 * 抽象Application事件多播
 * 多播即依次唤醒事件监听器，所以需要存储事件监听器
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

	//存储事件监听器，当事件发生时，会依次执行监听器的处理函数
	public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new HashSet<>();

	private BeanFactory beanFactory;

	@Override
	public void addApplicationListener(ApplicationListener<?> listener) {
		applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
	}

	@Override
	public void removeApplicationListener(ApplicationListener<?> listener) {
		applicationListeners.remove(listener);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
