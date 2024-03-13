package com.my.beans.factory;

import com.my.beans.BeansException;

import java.util.Map;

/**
 * 增加对bean容器做列表操作（查询并返回特定的值）的功能
 */
public interface ListableBeanFactory extends BeanFactory {

	/**
	 * 返回指定类型的所有实例
	 *
	 * @param type
	 * @param <T>
	 * @return
	 * @throws BeansException
	 */
	<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

	/**
	 * 返回定义的所有bean的名称
	 *
	 * @return
	 */
	String[] getBeanDefinitionNames();
}
