package com.framework.utils;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.framework.api.restServices.core.RestResponse;
import com.framework.api.soapServices.core.SoapService;
import com.framework.utils.assertions.TestAssertions;
import com.framework.utils.reporting.ApiReporter;
import com.framework.utils.reporting.ConsoleReporter;
import com.framework.utils.reporting.DriverReporter;
import com.framework.utils.reporting.ReporterUtilities;
import com.framework.utils.reporting.TestNgReporter;
import com.framework.utils.utilities.StackTraceInfo;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

import static com.framework.utils.reporting.ReporterConstants.*;
import static com.framework.utils.assertions.AssertionStatus.*;

public class TestReporter {
	
    private static int debugLevel = 0;
    
    public static final int NONE  = 0;
	public static final int INFO  = 1;
	public static final int DEBUG = 2;
	public static final int TRACE = 3;
	public static final int ERROR = 4;

    public static void setDebugLevel(int level) {
        debugLevel = level;
    }

    public static int getDebugLevel() {
        return debugLevel;
    }

    public static String getTimestamp() {
       return ReporterUtilities.getTimestamp();
    }

    public static String trimHtml(String log) {
        return ReporterUtilities.trimHtml(log);
    }

    public static void setPrintToConsole(boolean printToConsole) {
        if (printToConsole)
        	ConsoleReporter.enablePrinting();
        else
        	ConsoleReporter.disablePrinting();
    }

    public static void setPrintFullClassPath(boolean printClassPath) {
        if (printClassPath)
        	ReporterUtilities.enableClassPathPrinting();
        else
        	ReporterUtilities.disableClassPathPrinting();
    }

    public static boolean getPrintFullClassPath() {
        return ReporterUtilities.getClassPathPrinting();
    }

    @Step("{step}")
    public static void logStep(String step) {
    	log("STEP: " + step);
    }

    @Step("{step}")
    public static void logTransaction(String step) {
    	log("PERFORMANCE: " + step);
    }

    @Description("{scenario}")
    public static void logScenario(String scenario) {
    	log("SCENARIO: " + scenario);
    }

    public static void interfaceLog(String message) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        logInfo(message);
    }

    public static void interfaceLog(String message, boolean status) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	String info = status ? "<font size=2 color='green'>" + message + "</font>"
    						   : "<font size=2 color='red'>" + message + "</font>";
    	
        logInfo(info);
    }

    @Step("{message}")
    public static void log(String message) {
    	logToConsoleAndTestNgReport(message);
    }
    
    public static void logToConsoleAndTestNgReport(String message) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        ConsoleReporter.printInfoText(message);
        TestNgReporter.print(message);
    }
    
    public static void logFailure(String assertion, String message) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	ConsoleReporter.print(assertion, message, FAIL);
    	TestNgReporter.print(assertion , message, FAIL);
    }
    
    public static void logFailure(String message) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	ConsoleReporter.printErrorText(message);
    	Reporter.log(String.format(getTimestamp(), message, RED_ROW));
    }
    
    public static void logPass(String assertion, String message) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	ConsoleReporter.print(assertion, message, PASS);
       	TestNgReporter.print(assertion , message, PASS);
    }

    public static void logTrace(String message) 	{
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	ConsoleReporter.printTraceText(message);		
    }
    
    public static void logInfo(String message) 		{		
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	ConsoleReporter.printInfoText(message);			
    }
    
    public static void logDebug(String message) 	{		
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	ConsoleReporter.printDebugText(message);		
    }
    
    
    //assertions
    
    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertTrue() instead.    
     */
    @Deprecated(since = "1.1.8.7-SNAPSHOT", forRemoval = true)
    public static void assertTrue(boolean condition, String description) {
    	TestAssertions.assertTrue(condition, description);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertFalse() instead.    
     */
    public static void assertFalse(boolean condition, String description) {
    	TestAssertions.assertFalse(condition, description);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertEquals() instead.    
     */
    public static void assertEquals(Object value1, Object value2, String description) {
    	TestAssertions.assertEquals(value1, value2, description);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertNotEquals() instead.    
     */
    public static void assertNotEquals(Object value1, Object value2, String description) {
    	TestAssertions.assertNotEquals(value1, value2, description);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertGreaterThanZero() instead.    
     */
    public static void assertGreaterThanZero(int value) {
    	TestAssertions.assertGreaterThanZero(value);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertGreaterThanZero() instead.    
     */
    public static void assertGreaterThanZero(float value) {
        TestAssertions.assertGreaterThanZero((int) value);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertGreaterThanZero() instead.    
     */
    public static void assertGreaterThanZero(double value) {
        TestAssertions.assertGreaterThanZero((int) value);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertNull() instead.    
     */
    public static void assertNull(Object value, String description) {
    	TestAssertions.assertNull(value, description);
    }
    
    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertNotNull() instead.    
     */
    public static void assertNotNull(Object value, String description) {
    	TestAssertions.assertNotNull(value, description);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.softAssertTrue() instead.    
     */
    public static boolean softAssertTrue(boolean condition, String description) {
    	return TestAssertions.softAssertTrue(condition, description);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.softAssertEquals() instead.    
     */
    public static boolean softAssertEquals(Object value1, Object value2, String description) {
    	return TestAssertions.softAssertEquals(value1, value2, description);
    }
    
    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.softAssertFalse() instead.    
     */
    public static boolean softAssertFalse(boolean condition, String description) {
    	return TestAssertions.softAssertFalse(condition, description);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.softAssertNull() instead.    
     */
    public static boolean softAssertNull(Object value, String description) {
    	return TestAssertions.softAssertNull(value, description);
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.softAssertNotNull() instead.    
     */
    public static boolean softAssertNotNull(Object value, String description) {
    	return TestAssertions.softAssertNotNull(value, description);
    }
    
    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertAll() instead.    
     */
    public static void assertAll() {
    	TestAssertions.assertAll();
    }

    /**
     * @deprecated
     * This method is no longer acceptable as assertion.
     * <p> Use TestAssertions.assertFail() instead.    
     */
    public static void assertFail(String description) {
    	TestAssertions.assertFail(description);
    }
    
    
    //api logging
    public static void logAPI(boolean pass, String message, SoapService bs) {
    	ApiReporter.logAPI(pass, message, bs);
    }

    public static void logAPI(boolean pass, String message, RestResponse rs) {
    	ApiReporter.logAPI(pass, message, rs);
    }
    
    public static void logNoHtmlTrim(String message) {
    	ApiReporter.logNoHtmlTrim(message);
    }

    public static void logNoXmlTrim(String message) {
    	ApiReporter.logNoHtmlTrim(message);    
    }


    //driver logging
    public static void logScreenshot(WebDriver driver) {
    	DriverReporter.logScreenshot(driver);
    }

    public static void logScreenshot(String testName, WebDriver driver) {
    	DriverReporter.logScreenshot(testName, driver);
    }

    public static String webDir( String testName ) {
    	return DriverReporter.webDir(testName);
    }
    
	public static boolean isOnJenkins() {
		return DriverReporter.isOnJenkins();
	}
	
	public static String jobURL() {
		return DriverReporter.jobURL();
	}

    public String analyzeLog(AllegisDriver driver) {
    	return DriverReporter.analyzeLog(driver);
    }

    public static void logConsoleErrors(AllegisDriver driver) {
    	DriverReporter.logConsoleErrors(driver);
    }

}