package com.framework.utils;

import org.springframework.context.ConfigurableApplicationContext;

import com.allegis.service.frameworklog.FrameworkAuditService;
import com.framework.utils.listeners.TestListener;

public class AuditLogger {

	private AuditLogger() {
		
	}
	
	public static void reset() {
		getFrameworkAuditService().reset();
	}
	
	public static void collect(String methodInfo) {
		getFrameworkAuditService().collectInfo(methodInfo);
	}
	
	public static void log(double executionTime) {
		getFrameworkAuditService().log(executionTime);
	}
	
	public static void log(String exceptionMessage, String stackTrace, double executionTime) {
		getFrameworkAuditService().log(exceptionMessage, stackTrace, executionTime);
	}
	
	private static FrameworkAuditService getFrameworkAuditService() {
		ConfigurableApplicationContext context = TestListener.getContext();
		FrameworkAuditService service = context.getBean(FrameworkAuditService.class);
		return service;
	}
	
}