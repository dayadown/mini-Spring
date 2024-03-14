package com.my.context;

import com.my.beans.factory.HierarchicalBeanFactory;
import com.my.beans.factory.ListableBeanFactory;
import com.my.core.io.ResourceLoader;

/**
 * 应用上下文，
 * 上下文 即可以应用到的bean后处理和bean信息后处理，bean容器会自动根据该上下文执行bean后处理和bean信息后处理
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {

}
