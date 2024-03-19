package com.my.context.event;

import com.my.context.ApplicationEvent;
import com.my.context.ApplicationListener;

/**
 * Application事件多播接口
 */
public interface ApplicationEventMulticaster {

	/**
	 * 增加事件监听器
	 * @param listener
	 */
	void addApplicationListener(ApplicationListener<?> listener);

	/**
	 * 移除事件监听器
	 * @param listener
	 */
	void removeApplicationListener(ApplicationListener<?> listener);

	void multicastEvent(ApplicationEvent event);

}
