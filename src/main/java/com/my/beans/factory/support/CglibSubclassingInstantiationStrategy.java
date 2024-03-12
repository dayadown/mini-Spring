package com.my.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import com.my.beans.BeansException;
import com.my.beans.factory.config.BeanDefinition;


/**
 * cglib动态实例化
 * cglib是一个用于动态代理的库
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

	/**
	 * 使用CGLIB动态生成子类
	 *
	 * @param beanDefinition
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
		Enhancer enhancer = new Enhancer();
		//设置代理目标类
		enhancer.setSuperclass(beanDefinition.getBeanClass());
		//设置拦截器，拦截器定义了当代理目标类的方法被被调用时应该执行的操作，这里只执行原操作
		//若要自定义一个拦截器类需要实现MethodInterceptor接口，这里使用了Lambda表达式实现了MethodInterceptor接口的intercept方法
		//obj 是被代理的对象。
		//method 是被调用的方法。
		//argsTemp 是方法的参数数组。
		//proxy 是用于调用目标方法的MethodProxy对象
		enhancer.setCallback((MethodInterceptor) (obj, method, argsTemp, proxy) -> proxy.invokeSuper(obj,argsTemp));
		//创建代理实例
		return enhancer.create();
	}
}
