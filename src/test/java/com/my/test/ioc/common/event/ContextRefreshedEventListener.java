package com.my.test.ioc.common.event;

import com.my.context.ApplicationListener;
import com.my.context.event.ContextRefreshedEvent;

/**
 * 上下文刷新完成监听，响应ContextRefreshedEvent
 */
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("监听到容器刷新了");
	}
}
