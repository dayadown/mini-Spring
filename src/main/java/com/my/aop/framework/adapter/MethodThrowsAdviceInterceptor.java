package com.my.aop.framework.adapter;


import com.my.aop.framework.advice.MethodThrowsAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodThrowsAdviceInterceptor implements MethodInterceptor {

    private MethodThrowsAdvice methodThrowsAdvice;

    public MethodThrowsAdviceInterceptor(){

    }

    public MethodThrowsAdviceInterceptor(MethodThrowsAdvice methodThrowsAdvice){
        this.methodThrowsAdvice=methodThrowsAdvice;
    }
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        }catch (Exception e){
            methodThrowsAdvice.whileThrows(invocation.getMethod(),invocation.getArguments(),invocation.getThis());
            throw new Exception("原方法抛出异常");
        }
    }
}
