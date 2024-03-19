package com.my.context.event;

import com.my.context.ApplicationContext;
import com.my.context.ApplicationEvent;

/**
 * 上下文事件类
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

	public ApplicationContextEvent(ApplicationContext source) {
		super(source);
	}

	public final ApplicationContext getApplicationContext() {
		return (ApplicationContext) getSource();
	}
}
