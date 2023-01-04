package com.framework.utils.utilities;

public class StackTraceInfo {		
	
	private StackTraceInfo() {		
	}
	
	public static String getMethodInfo() {
	    	
	    StackTraceElement element = getStackTrace()[3];
	    
	    String methodName = element.getMethodName();
	    String className  = element.getClassName();
	    	
	    int i = className.lastIndexOf(".");
	    className = className.substring(i+1);
	    	
	    return String.format("( %s - %s ), ", className, methodName);

	}
	
	public static StackTraceElement[] getStackTrace() {
		return Thread.currentThread().getStackTrace();
	}

}
