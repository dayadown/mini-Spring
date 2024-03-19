package com.my.context;

import java.util.EventListener;

/**
 * 监听器泛型接口，对某一特定类型的事件的监听器
 * @param <E>
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	/**
	 * 监听处理函数
	 * @param event
	 */
	void onApplicationEvent(E event);
}