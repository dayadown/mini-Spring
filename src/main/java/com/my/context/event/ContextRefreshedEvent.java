package com.my.context.event;

import com.my.context.ApplicationContext;

/**
 * 上下文刷新事件
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

	public ContextRefreshedEvent(ApplicationContext source) {
		super(source);
	}
}
