package com.my.aop.aspectj;

import lombok.Setter;
import org.aopalliance.aop.Advice;
import com.my.aop.Pointcut;
import com.my.aop.PointcutAdvisor;

/**
 * aspectJ表达式的advisor
 * 结合了advice和切点
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

	private AspectJExpressionPointcut pointcut;

	@Setter
	private Advice advice;

	@Setter
	private String expression;


	@Override
	public Pointcut getPointcut() {
		if(this.pointcut==null)
			this.pointcut=new AspectJExpressionPointcut(expression);
		return pointcut;
	}

	@Override
	public Advice getAdvice() {
		return advice;
	}

}
