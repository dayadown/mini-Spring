package com.my.test.ioc;

import com.my.beans.factory.DisposableBean;
import com.my.beans.factory.InitializingBean;
import lombok.Data;

@Data
public class Person1 implements InitializingBean, DisposableBean {
    private String name;

    private Integer age;

    //依赖的Car是另一个bean
    private Car car;

    @Override
    public void destroy() throws Exception {
        System.out.println("由于实现接口而存在的销毁方法执行了");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("由于实现接口而存在的初始化方法执行了");
    }

    public void customInitMethod() {
        System.out.println("自定义初始化方法执行了");
    }

    public void customDestroyMethod() {
        System.out.println("自定义销毁方法执行了");
    }
}
