package com.my.beans.factory.config;

import com.my.beans.BeansException;
import com.my.beans.factory.BeanFactory;


/**
 * 具有自动装配能力（即自动得修饰（装饰配置）bean，即在bean初始化前后执行执行一些方法修饰该bean（或是代理），
 * 之后再把bean放入bean容器）的bean容器接口
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
    /**
     * 执行BeanPostProcessors的postProcessBeforeInitialization方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;

    /**
     * 执行BeanPostProcessors的postProcessAfterInitialization方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;
}
