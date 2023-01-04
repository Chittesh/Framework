package com.framework.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExceptionErrorMapping {

	private static final String ELEMENT_SYNCHRONIZATION_ERROR = "Element Synchronization error";
	private static final String CONFIGURATION_ERROR 		  = "Configuration Error";
	private static final String ENVIRONMENT_ERROR   		  = "Environment Errors";
	private static final String ASSERTION_FAILURE   		  = "Assertion Failures";
	private static final String CODING_ERROR        		  = "Coding Error";

	private static final List<ErrorInfo> ERROR_LIST_INFO = 
		Arrays.asList(
			new ErrorInfo("com.framework.api.soapServices.core.exceptions", "API error", CODING_ERROR),
			new ErrorInfo("org.testng.SkipException", "Invalid Parameter error", CODING_ERROR),
			new ErrorInfo("java.lang.AssertionError", "Assertion error", ASSERTION_FAILURE),
			new ErrorInfo("org.openqa.selenium.TimeoutException: timeout: Timed out receiving message from renderer", "Environment not responding", ENVIRONMENT_ERROR),
			new ErrorInfo("java.util.MissingResourceException", "Not Found Property error", CONFIGURATION_ERROR),
			new ErrorInfo("org.openqa.selenium.JavascriptException", "Javascript error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.StaleElementReferenceException", "Stale Element error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.NoSuchWindowException", "No window found error", CODING_ERROR),
			new ErrorInfo("com.framework.exception.automation.ElementNotFoundInAnyFrameException", "Element Not Found In Any Frame error", CODING_ERROR),
			new ErrorInfo("java.lang.ArrayIndexOutOfBoundsException", "Array Out Of Bounds error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.TimeoutException: Expected condition failed: waiting for title to be", "Incorrect Page Title error", CODING_ERROR),
			new ErrorInfo("JsonException", "JSON Content error", CODING_ERROR), 
			new ErrorInfo("java.net.ConnectException: Connection timed out", "HTTP Connection Timeout error", ENVIRONMENT_ERROR),
			new ErrorInfo("org.openqa.selenium.TimeoutException: Expected condition failed: waiting for visibility", "Element Not Visible error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.TimeoutException: Expected condition failed: waiting for element", ELEMENT_SYNCHRONIZATION_ERROR, CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.WebDriverException", "Unknown Selenium error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.ElementClickInterceptedException: element click intercepted", "Element Not Clickable error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.ElementNotInteractableException: element not interactable", "Element Not Interactable error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.NotFoundException", "Element Not Found error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.NoSuchSessionException", "No Session error", ENVIRONMENT_ERROR),
			new ErrorInfo("IllegalArgumentException", "Illegal Argument error", CODING_ERROR),
			new ErrorInfo("InvalidArgumentException", "Invalid Argument error", CODING_ERROR),
			new ErrorInfo("prg.xml.sax.SAXParseException", "XML Parse error", CODING_ERROR),
			new ErrorInfo("java.lang.Error: Unresolved compilation problem", "Java Compilation error", CODING_ERROR),
			new ErrorInfo("java.lang.reflect.UndeclaredThrowableException", "Java error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.NoSuchElementException", "Element Not Found error", CODING_ERROR),
			new ErrorInfo("java.lang.IndexOutOfBoundsException", "Index Ouf Of Bounds error", CODING_ERROR), 
			new ErrorInfo("java.lang.StringIndexOutOfBoundsException", "String Out Of Bounds error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.UnhandledAlertException", "Unexpected Alert Open error", CODING_ERROR),
			new ErrorInfo("io.restassured.path.json.exception.JsonPathException", "Failed To Parse JSON Document error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.TimeoutException: Expected condition failed: waiting for presence of element", ELEMENT_SYNCHRONIZATION_ERROR, CODING_ERROR),
			new ErrorInfo("java.lang.NoClassDefFoundError", "Java Class Not Found error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.ScriptTimeoutException", "Script Timeout exception", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.InvalidElementStateException", "Invalid Element State error", CODING_ERROR),
			new ErrorInfo("java.lang.NoSuchMethodError", "Java No Such Method error", CODING_ERROR),
			new ErrorInfo("java.net.SocketTimeoutException", "Socket Timeout error", ENVIRONMENT_ERROR),
			new ErrorInfo("java.io.FileNotFoundException", "File Not Found error", ENVIRONMENT_ERROR),
			new ErrorInfo("org.openqa.selenium.remote.UnreachableBrowserException", "Unreachable Browser error", ENVIRONMENT_ERROR),
			new ErrorInfo("java.lang.NumberFormatException", "Java Number Format error", CODING_ERROR),
			new ErrorInfo("java.lang.ExceptionInInitializerError", "Java error", CODING_ERROR),
			new ErrorInfo("com.framework.utils.listeners.InvalidVersionOnIdException", "Invalid V1 Id error", CONFIGURATION_ERROR),
			new ErrorInfo("com.framework.api.restServices.exceptions.RestException", "Rest API error", CODING_ERROR), 
			new ErrorInfo("org.openqa.selenium.TimeoutException: Expected condition failed: waiting for text", ELEMENT_SYNCHRONIZATION_ERROR, CODING_ERROR),
			new ErrorInfo("javax.net.ssl.SSLHandshakeException", "SSL error", ENVIRONMENT_ERROR),
			new ErrorInfo("java.io.IOException: Server returned HTTP response code: 500", "HTTP 500 - Internal Server Error", ENVIRONMENT_ERROR),
			new ErrorInfo("java.net.ConnectException: Connection refused", "Connection Refused", ENVIRONMENT_ERROR),
			new ErrorInfo("java.nio.file.FileSystemException", "File In Use error", ENVIRONMENT_ERROR), 
			new ErrorInfo("java.lang.ClassCastException", "Java Class Cast error", CODING_ERROR),
			new ErrorInfo("Automation Error: SObject not available", "Java Object Not Available error", CODING_ERROR),
			new ErrorInfo("org.openqa.selenium.interactions.MoveTargetOutOfBoundsException", "Move Target Out Of Bounds error", CODING_ERROR));
	
	private ExceptionErrorMapping() {
		
	}
	
	public static ErrorInfo emptyError() {
		return new ErrorInfo("", "", "");
	}
	
	public static List<ErrorInfo> getMapping() {

		return ERROR_LIST_INFO;
		
	}
	
}
