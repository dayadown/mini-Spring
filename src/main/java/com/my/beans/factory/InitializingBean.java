package com.my.beans.factory;

/**
 * 初始化方法，想要拥有初始化方法需实现该接口，因为代码是根据固定方法名来识别初始化方法
 */
public interface InitializingBean {

	void afterPropertiesSet() throws Exception;
}
