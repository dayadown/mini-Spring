package com.my.beans.factory.config;

/**
 * 包装bean作为属性的类，方便判断属性是否为bean，bean作为属性时，在声明bean信息时属性应该使用该类的对象
 * 在为一个bean注入属性时，若属性值的对象类型为BeanReference，则其要引用另一个bean，先去创建另一个bean，暂未处理循环依赖
 */
public class BeanReference {

	//依赖的bean名
	private final String beanName;


	public BeanReference(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return beanName;
	}
}
