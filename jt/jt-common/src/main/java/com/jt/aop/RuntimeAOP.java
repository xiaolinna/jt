package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RuntimeAOP {

	@Around("execution(* com.jt.service..*.*(..))")
//	@Around("bean(*service*)")
	public Object around(ProceedingJoinPoint pj) {
		
		Long startTime = System.currentTimeMillis();
		Object result = null;
		try {
			result = pj.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		Long endTime = System.currentTimeMillis();
		Class<?> class1 = pj.getTarget().getClass();
		System.out.println(class1);
		String name = pj.getSignature().getName();
		System.out.println(name);
		System.out.println("耗时"+(endTime - startTime) + "ms");
		return result;
	}
	
}
