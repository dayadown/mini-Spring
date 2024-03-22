package com.my.test.aop;

/**
 * @author derekyi
 * @date 2020/12/6
 */
public class WorldServiceImpl implements WorldService {

	@Override
	public void explode() {
		//异常通知测试方法
		//int a=1/0;
		System.out.println("原方法执行");
	}
}
