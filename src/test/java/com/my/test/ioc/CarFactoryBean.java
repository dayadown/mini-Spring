package com.my.test.ioc;

import com.my.beans.factory.FactoryBean;
import lombok.Data;

/**
 * FactoryBean测试
 */

@Data
public class CarFactoryBean implements FactoryBean<Car> {

	private String brand;

	@Override
	public Car getObject() throws Exception {
		Car car = new Car();
		car.setBrand(brand);
		return car;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
