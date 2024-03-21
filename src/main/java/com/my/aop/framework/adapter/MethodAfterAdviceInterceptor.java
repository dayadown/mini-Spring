package com.my.aop.framework.adapter;

import com.my.aop.framework.advice.MethodAfterAdvice;
import com.my.aop.framework.advice.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 后置通知拦截器
 */
public class MethodAfterAdviceInterceptor implements MethodInterceptor {
    private MethodAfterAdvice methodAfterAdvice;
    public MethodAfterAdviceInterceptor(MethodAfterAdvice methodAfterAdvice){
        this.methodAfterAdvice=methodAfterAdvice;
    }
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //调用原方法
        Object object=invocation.proceed();
        //后置通知
        methodAfterAdvice.after(invocation.getMethod(),invocation.getArguments(),invocation.getThis());
        return object;
    }
}
