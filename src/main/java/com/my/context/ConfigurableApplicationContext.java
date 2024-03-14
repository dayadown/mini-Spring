package com.my.context;

import com.my.beans.BeansException;


public interface ConfigurableApplicationContext extends ApplicationContext {

	/**
	 * 刷新容器
	 *
	 * @throws BeansException
	 */
	void refresh() throws BeansException;
}
