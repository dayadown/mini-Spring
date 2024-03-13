package com.my.beans.factory;


import com.my.beans.BeansException;

/**
 * bean容器接口
 * （bean容器其实就是一个所谓bean工厂（这个工厂不单单是生产），生产，销毁，管理bean的）
 * 抽象来看，用户就只需要从工厂里拿一个bean，所以最高的抽象就是bean工厂只有getBean方法至于工厂内部如何生产，就看BeanFactory的各种实现了
 */
public interface BeanFactory {
	/**
	 * 获取bean
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	Object getBean(String name) throws BeansException;

	/**
	 * 根据名称和类型查找bean
	 *
	 * @param name
	 * @param requiredType
	 * @param <T>
	 * @return
	 * @throws BeansException
	 */
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;
}
