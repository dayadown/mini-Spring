package com.my.aop.framework;

import com.my.aop.AdvisedSupport;


/**
 * 代理工厂类，由工厂类生成代理对象
 */
public class ProxyFactory {

	private AdvisedSupport advisedSupport;

	public ProxyFactory(AdvisedSupport advisedSupport) {
		this.advisedSupport = advisedSupport;
	}

	public Object getProxy() {
		return createAopProxy().getProxy();
	}

	private AopProxy createAopProxy() {
		if (advisedSupport.isProxyTargetClass()) {
			return new CglibAopProxy(advisedSupport);
		}

		return new JdkDynamicAopProxy(advisedSupport);
	}
}
