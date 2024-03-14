package com.my.beans.factory.config;

import com.my.beans.BeansException;
import com.my.beans.factory.HierarchicalBeanFactory;


/**
 * 可配置的bean容器接口，（可配置指可通过后处理程序改bean实例，而后处理程序是可以人为配置的）
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
    /**
     * 向bean后处理队列中加入后处理对象
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
