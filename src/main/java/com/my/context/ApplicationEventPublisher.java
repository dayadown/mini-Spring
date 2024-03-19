package com.my.context;

/**
 * 事件发布者接口,发布事件即调用多播器的多播函数，多播函数用此事件唤醒监听器
 */
public interface ApplicationEventPublisher {

	/**
	 * 发布事件
	 * @param event
	 */
	void publishEvent(ApplicationEvent event);
}
