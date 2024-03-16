package com.my.context;

import com.my.beans.BeansException;
import com.my.beans.factory.Aware;

/**
 * 实现该接口，能感知所属ApplicationContext(操作这个beanFactory的扩展)
 */
public interface ApplicationContextAware extends Aware {

	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
