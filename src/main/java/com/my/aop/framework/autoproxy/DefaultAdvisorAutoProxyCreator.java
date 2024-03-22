package com.my.aop.framework.autoproxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import com.my.aop.*;
import com.my.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.my.aop.framework.ProxyFactory;
import com.my.beans.BeansException;
import com.my.beans.factory.BeanFactory;
import com.my.beans.factory.BeanFactoryAware;
import com.my.beans.factory.config.BeanDefinition;
import com.my.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.my.beans.factory.support.DefaultListableBeanFactory;

import java.util.Collection;


/**
 * 自动根据bean容器中设置的Advisor(切点和通知的结合体)，创建代理对象
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

	private DefaultListableBeanFactory beanFactory;

	/**
	 * 根据类和beanName，判断类是否在切点表达式中，若在，则返回代理对象，若不在，则返回空
	 * @param beanClass
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		//避免死循环
		if (isInfrastructureClass(beanClass)) {
			return null;
		}

		// 获取配置的切点通知结合体
		// 当在单例化(getBean,然后发现没有，再createBean)AspectJExpressionPointcutAdvisor这个bean时，也会进行是否在切点内的判断，也就是也会进入到这个函数，
		// 在判断的途中，执行这一句代码时，bean容器中并没有AspectJExpressionPointcutAdvisor这个bean
		// getBean,createBean之前又要执行判断是否在切点内，形成一个无限的循环了
		// 所以单例化AspectJExpressionPointcutAdvisor这个bean时，应该直接走Spring创建的流程
		// AspectJExpressionPointcutAdvisor用于判断某类是否在切点内
		Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
		try {
			for (AspectJExpressionPointcutAdvisor advisor : advisors) {
				//获取类选择器
				ClassFilter classFilter = advisor.getPointcut().getClassFilter();
				//如果该bean类在切点表达式中，则创建一个代理对象并返回
				if (classFilter.matches(beanClass)) {
					//创建代理支持
					AdvisedSupport advisedSupport = new AdvisedSupport();

					//获取bean信息
					BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
					//创建该类的对象
					Object bean = beanFactory.getInstantiationStrategy().instantiate(beanDefinition);

					TargetSource targetSource = new TargetSource(bean);

					//为代理支持添加属性值
					advisedSupport.setTargetSource(targetSource);
					advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
					advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

					//返回代理对象
					return new ProxyFactory(advisedSupport).getProxy();
				}
			}
		} catch (Exception ex) {
			throw new BeansException("Error create proxy bean for: " + beanName, ex);
		}
		return null;
	}

	private boolean isInfrastructureClass(Class<?> beanClass) {
		return Advice.class.isAssignableFrom(beanClass)
				|| Pointcut.class.isAssignableFrom(beanClass)
				|| Advisor.class.isAssignableFrom(beanClass);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
