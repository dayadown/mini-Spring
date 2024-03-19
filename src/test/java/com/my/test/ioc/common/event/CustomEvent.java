package com.my.test.ioc.common.event;

import com.my.context.ApplicationContext;
import com.my.context.event.ApplicationContextEvent;

/**
 * 自定义事件
 */
public class CustomEvent extends ApplicationContextEvent {

	public CustomEvent(ApplicationContext source) {
		super(source);
	}
}
