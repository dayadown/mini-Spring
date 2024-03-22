package com.my.test.aop;

public class HelloServiceImpl implements HelloService{
    @Override
    public void sayHello() {
        System.out.println("hello!");
    }
}
