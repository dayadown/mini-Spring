package com.my.test.ioc.common.event;

import com.my.context.ApplicationListener;

/**
 * 自定义事件监听，响应CustomEvent
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

	@Override
	public void onApplicationEvent(CustomEvent event) {
		System.out.println("监听到自定义事件发生了");
	}
}
