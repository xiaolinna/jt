package com.jt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExceptionAOP {

	
	@AfterThrowing(pointcut = "execution(* com.jt.service..*.*(..))",throwing = "e")
	public void throwable(JoinPoint jp,Throwable e) {
		
		Class<?> class1 = e.getClass();
		StackTraceElement stackTraceElement = e.getStackTrace()[0];
		String message = e.getMessage();
		Class<?> class2 = jp.getTarget().getClass();
		String name = jp.getSignature().getName();
		
		System.out.println("异常类型:"+class1);
		System.out.println("异常信息:"+ message);
		System.out.println("目标对象类型:"+class2);
		System.out.println("目标方法名称:"+name);
	}
	
}
