package com.my.test.ioc;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class Person {
    private String name;

    private Integer age;

    //依赖的Car时另一个bean
    private Car car;
}
