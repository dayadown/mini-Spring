package com.my.test.ioc.common.event;

import com.my.context.ApplicationListener;
import com.my.context.event.ContextClosedEvent;

/**
 * 上下文关闭监听，响应ContextClosedEvent
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.out.println("监听到容器关闭了");
	}
}
