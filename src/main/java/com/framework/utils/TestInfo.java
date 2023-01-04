package com.framework.utils;

import static java.util.stream.Collectors.joining;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class TestInfo {
	
	private ITestResult testResult;
	private ITestNGMethod testMethod;
	
	public TestInfo(ITestResult testResult) {
		this.testResult = testResult;
		this.testMethod = testResult.getMethod();
	}

	public String getStatus() {
    	String status = "";
    	
    	switch (this.testResult.getStatus()) {
    			
    		case 1:
    			status = "SUCCESS";
    			break;
    			
    		case 2:
    			status = "FAILURE";
    			break;
    			
    		case 3:
    			status = "SKIP";
    			break;

    		default:
    			status = "UNKNOWN";
    			break;
    			
    	}
    	
    	return status;
    }
	
	public String getMethodQualifiedName() {
		return this.testMethod.getQualifiedName();
	}
	
	public String getMethodName() {
		return testMethod.getMethodName();
	}
	
	public String getFullTestClassName() {
		return testMethod.getTestClass().getName();
	}
	
	public String getTestClassName() {
		String fullTestClassName = getFullTestClassName();
		
		int i = fullTestClassName.lastIndexOf(".");
		
		return fullTestClassName.substring(i + 1);
	}
	
	public String getPackageName() {
		return getFullTestClassName().replace(getTestClassName(), "");
	}
	
	public String getTestGroups() {
		return String.join(",", testMethod.getGroups());
	}
	
	public long getExecutionTime() {
		return this.testResult.getEndMillis() - this.testResult.getStartMillis();
	}
	   	
	public String getV1TestId() {
		List<Annotation> annotations = getAnnotations();
		
		if (annotations.isEmpty())
			return "";
		
		String annotationValue = StringUtils.substringBetween(annotations.get(1).toString(), "(", ")");
		String[] values        = annotationValue.split(",");
	
		Optional<String> v1TestIdOptional = Arrays.asList(values).stream()
																 .filter(v -> v.contains("versionOneTestID"))
																 .findFirst();
		
		if (Boolean.FALSE.equals(v1TestIdOptional.isPresent()))
			return "";
		
		String v1TestId = StringUtils.substringBetween(v1TestIdOptional.get(), "{", "}").replace("\"", "");
		return v1TestId;
	}
	
	public String getException() {
		if (testResult.isSuccess())
			return "";
		
		Throwable throwable = this.getThrowable();
    	return throwable != null ? throwable.toString() : "";
	}
	
	public String getStackTrace() {
		
		if (this.getThrowable() == null) 
			return "";

		try (Stream<StackTraceElement> stream = Arrays.stream(this.getThrowable().getStackTrace())) {
			return stream.map(StackTraceElement::toString).collect(joining("\n"));
		}
		
	}
	
	public Date getStartDate() {
		long timestamp = testResult.getStartMillis(); 
		return timestamp == 0 ? new Date() : new Date(timestamp);
	}
	
	public Date getEndDate() {
		long timestamp = testResult.getEndMillis(); 
		return timestamp == 0 ? new Date() : new Date(timestamp);
	}
	
	public Throwable getThrowable() {
		return this.testResult.getThrowable();
	}

	@SuppressWarnings("unchecked")
	private List<Annotation> getAnnotations() {
		
		try {
			return Arrays.asList(testMethod.getRealClass()
										   .getDeclaredMethod(getMethodName())
										   .getAnnotations());
		} catch (NoSuchMethodException | SecurityException e) {
			return new ArrayList<>();
		}

	}
}
