package com.my.context.event;

import com.my.beans.BeansException;
import com.my.beans.factory.BeanFactory;
import com.my.context.ApplicationEvent;
import com.my.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 事件多播器（事件多播即对某一事件，依次唤醒监听器）
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

	public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
		setBeanFactory(beanFactory);
	}

	@Override
	public void multicastEvent(ApplicationEvent event) {
		for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
			if (supportsEvent(applicationListener, event)) {
				//执行监听处理函数
				applicationListener.onApplicationEvent(event);
			}
		}
	}

	/**
	 * 监听器是否对该事件感兴趣，也就是监听器是否在监听该事件类型的事件
	 *
	 * @param applicationListener
	 * @param event
	 * @return
	 */
	protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
		//.getGenericInterfaces()获取该类所实现的所有泛型接口，[0]:获取实现的第一个泛型接口
		//ApplicationListener实现的泛型接口的泛型类型即是该监听器的监听的事件类型
		Type type = applicationListener.getClass().getGenericInterfaces()[0];
		// 获取这个泛型接口的实际类型参数的第一个参数
		Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
		String className = actualTypeArgument.getTypeName();
		Class<?> eventClassName;
		try {
			eventClassName = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new BeansException("wrong event class name: " + className);
		}
		return eventClassName.isAssignableFrom(event.getClass());
	}
}
