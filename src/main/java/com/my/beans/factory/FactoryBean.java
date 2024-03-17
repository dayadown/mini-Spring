package com.my.beans.factory;

/**
 * 一种特殊的bean，实现该接口的bean类会存在于单例池中
 * 但是getBean的返回并不是该bean本身，而是实现的接口方法getObject的返回值
 * getBean流程：
 * 	先根据id或name查单例池
 * 		1.若查到了，判断其是否为FactoryBean
 * 			1.是FactoryBean，根据beanName查“getObject返回值池”
 * 				1.查到了，返回即可
 * 				2.没查到，调用bean内部的getObject方法，判断bean内部的isSingleton是否为true
 * 					1.为true,说明该返回值要复用，则将<beanName,getObject返回值>放入“getObject返回值池”中
 * 					2.为false,直接返回该返回值
 * 			2.不是FactoryBean，返回查到的bean即可
 * 		2.没查到，创建该bean，若bean为单例，放入单例池，不是就放，创建完后判断其是否为FactoryBean
 * 			1.是FactoryBean，根据beanName查“getObject返回值池”
 *  * 				1.查到了，返回即可
 *  * 				2.没查到，调用bean内部的getObject方法，判断bean内部的isSingleton是否为true
 *  * 					1.为true,说明该返回值要复用，则将<beanName,getObject返回值>放入“getObject返回值池”中
 *  * 					2.为false,直接返回该返回值
 *  * 			2.不是FactoryBean，返回查到的bean即可
 * @param <T>
 */
public interface FactoryBean<T> {

	T getObject() throws Exception;

	boolean isSingleton();
}
