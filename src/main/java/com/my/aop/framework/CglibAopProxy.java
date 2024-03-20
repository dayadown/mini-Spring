package com.my.aop.framework;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import com.my.aop.AdvisedSupport;

import java.lang.reflect.Method;

/**
 * cgli动态代理,根据AdvisedSupport生成代理对象
 */
public class CglibAopProxy implements AopProxy {

	private final AdvisedSupport advised;

	public CglibAopProxy(AdvisedSupport advised) {
		this.advised = advised;
	}


	@Override
	public Object getProxy() {
		Enhancer enhancer = new Enhancer();
		//设置要增强的父类
		enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
		//设置增强的类需要实现的接口
		enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
		//设置回调对象，该对象实现的intercept方法会在增强类的方法被调用时被执行
		enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
		//返回这个增强对象，增强对象包含原对象的所有的方法，但是在方法执行时DynamicAdvisedInterceptor也会执行
		return enhancer.create();
	}

	/**
	 * 注意此处的MethodInterceptor是cglib中的接口，advised中的MethodInterceptor的AOP联盟中定义的接口，因此定义此类做适配
	 * MethodInterceptor接口继承了Callback，enhancer.setCallback需要传入Callback
	 */
	private static class DynamicAdvisedInterceptor implements MethodInterceptor {

		private final AdvisedSupport advised;

		private DynamicAdvisedInterceptor(AdvisedSupport advised) {
			this.advised = advised;
		}

		/**
		 *
		 * @param o "this", the enhanced object
		 * @param method intercepted Method
		 * @param objects argument array; primitive types are wrapped
		 * @param methodProxy used to invoke super (non-intercepted method); may be called
		 * as many times as needed
		 * @return
		 * @throws Throwable
		 *
		 * 增强对象的方法执行前，会跳转执行这个函数
		 * 四个参数分别为1.调用的增强对象实体，2.调用的方法 3.参数 4.MethodProxy对象。即原始方法
		 */
		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, objects, methodProxy);
			if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
				//使用方法拦截器的方法实现代理逻辑
				return advised.getMethodInterceptor().invoke(methodInvocation);
			}
			return methodInvocation.proceed();
		}
	}

	/**
	 * 反射调用原对象的原方法，对方法拦截器的参数做类型适配
	 * 拦截器的invoke需要传入MethodInvocation（一个实现了MethodInvocation接口的类，类的proceed的方法实现），
	 * 而methodProxy（原始方法）需要包装近该接口类中，并实现proceed方法实现对原始方法的调用
	 */
	private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

		private final MethodProxy methodProxy;

		public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
			super(target, method, arguments);
			this.methodProxy = methodProxy;
		}

		/**
		 * 调用
		 * @return
		 * @throws Throwable
		 */
		@Override
		public Object proceed() throws Throwable {
			return this.methodProxy.invoke(this.target, this.arguments);
		}
	}
}
