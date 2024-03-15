package com.my.beans.factory.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.my.beans.BeansException;
import com.my.beans.factory.DisposableBean;
import com.my.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

/**
 * 销毁bean的适配器（也实现了DisposableBean），将有销毁方法的bean封装到适配器中，并放入一个特殊的池，将执行销毁方法时
 * 会遍历这个特殊的池进行执行销毁方法，而不需要从头到尾遍历单例池判断其类型是否是实现了DisposableBean或是否有自定义销毁方法再做销毁，
 * 带初始化方法的bean就没必要分开了，因为最终总要全部遍历一遍（若分开：不带初始化方法的要执行前置和后置，带初始化方法的放进特殊bean容器
 * 最后还要要拿出来执行前置-初始化-后置，和不分开的代价一样）
 *
 */
public class DisposableBeanAdapter implements DisposableBean {

	private final Object bean;

	private final String beanName;

	private final String destroyMethodName;

	public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
		this.bean = bean;
		this.beanName = beanName;
		this.destroyMethodName = beanDefinition.getDestroyMethodName();
	}

	/**
	 * 销毁方法，同样执行两个
	 * @throws Exception
	 */
	@Override
	public void destroy() throws Exception {
		//因实现了接口而有的销毁方法
		if (bean instanceof DisposableBean) {
			((DisposableBean) bean).destroy();
		}

		//避免同时继承自DisposableBean，且自定义方法与DisposableBean方法同名，销毁方法执行两次的情况
		//TODO 由于bean类实现DisposableBean会重写destroy方法，所以用户自定义的销毁方法的方法名不可能为destroy,这里防止的是xml中配置的自定义销毁方法的名字写成了destroy
		//TODO 如果xml中的定义的销毁方法的名字写为destroy就会执行两遍实现DisposableBean重写的destroy方法
		if (StrUtil.isNotEmpty(destroyMethodName) && !(bean instanceof DisposableBean && "destroy".equals(this.destroyMethodName))) {
			//执行通过反射获取自定义销毁方法
			Method destroyMethod = ClassUtil.getPublicMethod(bean.getClass(), destroyMethodName);
			if (destroyMethod == null) {
				throw new BeansException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
			}
			//执行该bean的自定义销毁方法
			destroyMethod.invoke(bean);
		}
	}
}
