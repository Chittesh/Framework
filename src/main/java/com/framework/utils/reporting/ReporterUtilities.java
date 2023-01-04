package com.framework.utils.reporting;

import com.framework.utils.date.SimpleDate;

public class ReporterUtilities {
	
	private static boolean printClassPath = true;
	 
	private ReporterUtilities() {
	}
	
	public static void enableClassPathPrinting() {
	     printClassPath = true;
	}
	
	public static void disableClassPathPrinting() {
	     printClassPath = false;
	}

	public static boolean getClassPathPrinting() {
	     return printClassPath;
	}
	
    public static String getClassPath(boolean printClassPath) {
        if (printClassPath) {
        	StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        	
            for (int i = 3; i < stackTraceElements.length; i++) {
            	StackTraceElement element = stackTraceElements[i];
            	
                String className  = element.getClassName();
                String methodName = element.getMethodName();

                if (!className.contains("sun.reflect") 
                	&& !className.contains("com.framework.utils.TestReporter")
                    && !className.contains("com.framework.utils.PageLoaded")
                    && !className.contains("java.lang.reflect") 
                    && !className.contains("java.lang.Thread") 
                    && !className.contains("com.sun.proxy") 
                    && !className.contains("org.testng.internal")
                    && !className.contains("java.util.concurrent.ThreadPoolExecutor")
                    && !className.contains("com.framework.utils.debugging")) 
                
                	return " > " +  className + "#" + methodName + " > ";
            }
        }
        
        return " > > ";
    }
    
	public static String getTimestamp() {			
		String date = SimpleDate.getTimestamp().toString().substring(11);
		return (date + "00").substring(0, 12) + " :: "; 
	}

	public static String trimHtml(String text) {
		return text != null ? text.replaceAll("<[^>]*>", "") : "";
	}

}
