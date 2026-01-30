package com.springweb.myweb.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Aspect
@Component
public class LoggingAspect {

    private final DataSource dataSource;

    LoggingAspect(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
	@Before("execution(* com..service.*.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		System.out.printf("-----> BEFORE: %s, %s \n",
				joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName());
	}
	
	@After("execution(* com..repository.*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		System.out.printf("-----> AFTER: %s, %s \n",
				joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName());
	}
	@Around("execution(* com..controllers.*.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		long start = System.currentTimeMillis(); // 현재 시간을 밀리초로 반환. 1/1000 초 단위.
		Object result = joinPoint.proceed(); // 실제 메서드 호출.
		long end = System.currentTimeMillis();
		System.out.printf("-----> Execution Lab : %s.%s : %d \n",
					joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(),
					(end - start)
				);
		return result;
	}
}
