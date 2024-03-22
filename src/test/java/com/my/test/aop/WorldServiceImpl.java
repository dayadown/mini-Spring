package com.my.test.aop;

import com.my.beans.BeansException;
import com.my.beans.factory.BeanFactory;
import com.my.beans.factory.BeanFactoryAware;
import com.my.context.ApplicationContext;
import com.my.context.ApplicationContextAware;


public class WorldServiceImpl implements WorldService{

	@Override
	public void explode() {
		//异常通知测试方法
		//int a=1/0;
		System.out.println("原方法执行");
	}

}
