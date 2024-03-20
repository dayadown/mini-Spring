package com.my.test.aop;

import com.my.aop.aspectj.AspectJExpressionPointcut;
import org.junit.Test;

import java.lang.reflect.Method;

public class AopTests {
    @Test
    public void testPointcutExpression() throws Exception {
        //定义切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.my.test.aop.Hello.*(..))");
        Class<Hello> clazz = Hello.class;
        Method method = clazz.getDeclaredMethod("sayHello");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method,clazz));

    }
}
