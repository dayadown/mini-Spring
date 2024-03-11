package com.my.test.ioc;

//这个类不加public就会报错，bean容器通过反射创建对象时会报没有权限的错
public class HelloService {
    public void sayHello() {
        System.out.println("hello");
    }
}

