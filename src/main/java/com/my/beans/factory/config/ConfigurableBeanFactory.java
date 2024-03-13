package com.my.beans.factory.config;

import com.my.beans.BeansException;
import com.my.beans.factory.HierarchicalBeanFactory;


/**
 * 可配置的bean容器接口，可往单例池加bean（可配置指可通过配置文件配置bean容器（即加bean减bean，改bean等））
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

}
