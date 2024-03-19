package com.my.context.event;

import com.my.context.ApplicationContext;

/**
 * 上下文销毁事件
 */
public class ContextClosedEvent extends ApplicationContextEvent {

	public ContextClosedEvent(ApplicationContext source) {
		super(source);
	}
}
