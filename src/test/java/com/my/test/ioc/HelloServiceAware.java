package com.my.test.ioc;

import com.my.beans.BeansException;
import com.my.beans.factory.BeanFactory;
import com.my.beans.factory.BeanFactoryAware;
import com.my.context.ApplicationContext;
import com.my.context.ApplicationContextAware;

public class HelloServiceAware implements BeanFactoryAware, ApplicationContextAware {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("感知到BeanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("感知到ApplicationContext");
    }
}
