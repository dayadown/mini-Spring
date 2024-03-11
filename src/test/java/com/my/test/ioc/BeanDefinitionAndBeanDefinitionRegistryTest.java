package com.my.test.ioc;

import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

/**
 *测试添加bean信息到bean容器中，并获取该bean
 */
public class BeanDefinitionAndBeanDefinitionRegistryTest {
    @Test
    public void testBeanFactory() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);
        beanFactory.registerBeanDefinition("helloService", beanDefinition);
        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        helloService.sayHello();
    }
}