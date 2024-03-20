package com.my.test.aop;

/**
 * @author derekyi
 * @date 2020/12/6
 */
public class WorldServiceImpl implements WorldService {

	@Override
	public void explode() {
		System.out.println("原方法执行");
	}
}
