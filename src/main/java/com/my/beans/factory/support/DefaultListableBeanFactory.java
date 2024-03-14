package com.my.beans.factory.support;

import com.my.beans.BeansException;
import com.my.beans.factory.ConfigurableListableBeanFactory;
import com.my.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *真正的bean容器类，不仅包含单例池，还包含bean信息容器
 * 有自动装配能力（即根据bean信息自动创建bean且放进单例池），有列表能力（查询），可配置（根据配置文件配置bean容器）
 * 有所有关于bean信息容器的功能
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

	//bean信息容器
	private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

	//实现bean信息容器相关的方法
	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(beanName, beanDefinition);
	}

	@Override
	public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
		BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
		if (beanDefinition == null) {
			throw new BeansException("No bean named '" + beanName + "' is defined");
		}

		return beanDefinition;
	}

	@Override
	public boolean containsBeanDefinition(String beanName) {
		return beanDefinitionMap.containsKey(beanName);
	}

	/**
	 * 从bean信息容器查找要取的类型的bean的名字，然后getBean即可，返回可能是多个
	 * @param type
	 * @return
	 * @param <T>
	 * @throws BeansException
	 */
	@Override
	public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
		Map<String, T> result = new HashMap<>();
		beanDefinitionMap.forEach((beanName, beanDefinition) -> {
			Class beanClass = beanDefinition.getBeanClass();
			if (type.isAssignableFrom(beanClass)) {
				T bean = (T) getBean(beanName);
				result.put(beanName, bean);
			}
		});
		return result;
	}

	@Override
	public String[] getBeanDefinitionNames() {
		Set<String> beanNames = beanDefinitionMap.keySet();
		return beanNames.toArray(new String[beanNames.size()]);
	}

	@Override
	public void preInstantiateSingletons() throws BeansException {
		beanDefinitionMap.keySet().forEach(this::getBean);
	}
}
