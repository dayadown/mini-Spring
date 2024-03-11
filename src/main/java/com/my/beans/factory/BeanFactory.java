package com.my.beans.factory;


import com.my.beans.BeansException;

/**
 * bean容器接口
 */
public interface BeanFactory {
	/**
	 * 获取bean
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	Object getBean(String name) throws BeansException;
}
