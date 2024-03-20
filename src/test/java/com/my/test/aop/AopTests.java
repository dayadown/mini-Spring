package com.my.test.aop;

import com.my.aop.aspectj.AspectJExpressionPointcut;
import com.my.aop.framework.JdkDynamicAopProxy;
import org.junit.Test;

import java.lang.reflect.Method;
import com.my.aop.*;

public class AopTests {
    /**
     * 切点测试，切点表达式解析，切点对类和方法的匹配
     * @throws Exception
     */
    @Test
    public void testPointcutExpression() throws Exception {
        //定义切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.my.test.aop.Hello.*(..))");
        Class<Hello> clazz = Hello.class;
        Method method = clazz.getDeclaredMethod("sayHello");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method,clazz));
    }

    @Test
    public void testJdkDynamicProxy() throws Exception {
        WorldService worldService = new WorldServiceImpl();

        //定义代理支持
        AdvisedSupport advisedSupport = new AdvisedSupport();
        //定义被代理的对象资源
        TargetSource targetSource = new TargetSource(worldService);
        //定义拦截器
        WorldServiceInterceptor methodInterceptor = new WorldServiceInterceptor();
        //定义方法选择器,选择com.my.test.aop包下，WorldService的explode方法，方法参数任意
        MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* com.my.test.aop.WorldService.explode(..))").getMethodMatcher();
        //为代理支持加入三个参数
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodInterceptor(methodInterceptor);
        advisedSupport.setMethodMatcher(methodMatcher);

        //创建代理对象，因为代理对象实现了和被代理对象的一样的接口，所以可以转换为被代理对象的接口类型
        WorldService proxy = (WorldService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }
}
