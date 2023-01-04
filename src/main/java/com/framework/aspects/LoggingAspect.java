package com.framework.aspects;

import java.time.Duration;
import java.time.LocalTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.framework.exception.ExceededMaximumExecutionTimeException;

@Aspect
public class LoggingAspect {

	private LocalTime startTime;
	
	private static final long MAX_RUNNING_TIME = 900;	
	
	private static final String TIME_ELLAPSED_MESSAGE = "\n\n   WARNING - the test exceeded %d seconds (actual time = %d seconds)!\n   Please consider simplyfing it or breaking it in multiple tests.\n\n";
	
	@Before("execution(* com.framework.utils.TestEnvironment.*Setup(*))")
	public void beforeSetupEnvironment(JoinPoint joinPoint) 
	{
		this.startTime = LocalTime.now();
	}
	
	@After("execution(* com.framework.core.interfaces.impl..*Impl.*(*))")
	public void afterElementImplMethodHavingAnyParameterType(JoinPoint joinPoint) 
	{
		failTestIfExceedsMaximumExecutionTime(joinPoint);
	}	

	@After("call(* com.framework.core.interfaces.impl..*.*())")
	public void afterElementImplMethodHavingAnyNumberOfParameters(JoinPoint joinPoint) 
	{
		failTestIfExceedsMaximumExecutionTime(joinPoint);
	}	

	@After("execution(* com.framework.core.interfaces.impl..*Impl.*(..))")
	public void afterElementImplMethodHavingNoParameters(JoinPoint joinPoint) 
	{
		failTestIfExceedsMaximumExecutionTime(joinPoint);
	}	
	
	private void failTestIfExceedsMaximumExecutionTime(JoinPoint joinPoint) 
	{

		long timeEllapsed = timeEllapsed()/1000;

		if (timeEllapsed > MAX_RUNNING_TIME) 
			throw new ExceededMaximumExecutionTimeException(String.format(TIME_ELLAPSED_MESSAGE, MAX_RUNNING_TIME, timeEllapsed));

	}	
	
	public long timeEllapsed() {
		if (startTime != null)
			return Duration.between(startTime, LocalTime.now()).toMillis();
		
		return Duration.ofMillis(0).toMillis();
	}
	
}
