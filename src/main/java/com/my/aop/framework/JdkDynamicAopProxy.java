package com.my.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import com.my.aop.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理，根据AdvisedSupport生成代理对象
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

	private final AdvisedSupport advised;

	public JdkDynamicAopProxy(AdvisedSupport advised) {
		this.advised = advised;
	}

	/**
	 * AopProxy的接口方法，返回代理对象
	 * @return
	 */
	@Override
	public Object getProxy() {
		//Proxy.newProxyInstance三个参数
		//1.类加载器 2.代理类将实现的接口列表，这写接口方法暴露给用户，一般是被代理类实现的接口，这样代理类和被代理类在用户使用看来无差别
		//3.一个实现了 InvocationHandler 接口的对象，它负责处理代理实例上的方法调用。InvocationHandler 接口有一个方法 invoke
			//invoke()方法有三个参数
				//1.调用的代理对象的实例
				//2.调用的方法
				//3.方法的参数列表
		return Proxy.newProxyInstance(
				getClass().getClassLoader(),
				advised.getTargetSource().getTargetClass(),
				this
		);
	}

	/**
	 * @param proxy the proxy instance that the method was invoked on
	 *
	 * @param method the {@code Method} instance corresponding to
	 * the interface method invoked on the proxy instance.  The declaring
	 * class of the {@code Method} object will be the interface that
	 * the method was declared in, which may be a superinterface of the
	 * proxy interface that the proxy class inherits the method through.
	 *
	 * @param args an array of objects containing the values of the
	 * arguments passed in the method invocation on the proxy instance,
	 * or {@code null} if interface method takes no arguments.
	 * Arguments of primitive types are wrapped in instances of the
	 * appropriate primitive wrapper class, such as
	 * {@code java.lang.Integer} or {@code java.lang.Boolean}.
	 *
	 * @return
	 * @throws Throwable
	 *
	 *
	 * 代理对象执行某一方法时会跳转到这里，因为代理对象不知道原对象的存在，所以需要用反射支持对象的数据来调用原对象方法
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//如果方法能在被代理对象中找到，则代理，否则直接放行
		//判断该方法是否是切点监视的方法（该方法是否满足切点表达式）
		if (advised.getMethodMatcher().matches(method)) {
			//获取拦截器
			MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
			//将原方法的反射调用传入，并执行拦截器的方法，拦截器实现了代理的逻辑
			return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args));

			//平时接触的并没有定义拦截器，直接在这个大括号里实现代理的逻辑，直接在代理返回那里定义匿名函数
		}
		return method.invoke(advised.getTargetSource().getTarget(), args);
	}
}
