package com.framework.utils;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.stream.Stream;

import org.testng.ITestResult;

public class StackTrace {
	
	private ITestResult testResult;

	public StackTrace(ITestResult testResult) {
		this.testResult = testResult;		
	}
	
	public String toString() {
		
		if (this.testResult.getThrowable() == null) 
			return "";

		try (Stream<StackTraceElement> stream = Arrays.stream(this.testResult.getThrowable().getStackTrace())) {
			return stream.map(StackTraceElement::toString).collect(joining("\n"));
		}
		
	}

}
