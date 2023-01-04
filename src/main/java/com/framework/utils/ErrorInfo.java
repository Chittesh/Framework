package com.framework.utils;

public class ErrorInfo {

	private String exception;
	private String errorType;
	private String errorCategory;
	
	public ErrorInfo(String exception, String errorType, String errorCategory) {
		this.exception = exception;
		this.errorType = errorType;
		this.errorCategory = errorCategory;
	}

	public String getException() {
		return exception;
	}

	public String getErrorType() {
		return errorType;
	}

	public String getErrorCategory() {
		return errorCategory;
	}
	
	
}
