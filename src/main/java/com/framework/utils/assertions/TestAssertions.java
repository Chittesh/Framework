package com.framework.utils.assertions;

import org.testng.Assert;

import com.framework.utils.AuditLogger;
import com.framework.utils.utilities.StackTraceInfo;
import com.framework.utils.TestReporter;
import com.framework.utils.reporting.TestNgReporter;
import com.framework.utils.reporting.ConsoleReporter;
import static com.framework.utils.reporting.ReporterConstants.*;
import static com.framework.utils.assertions.AssertionStatus.*;

import io.qameta.allure.Step;


public class TestAssertions {

	private static ThreadLocal<Boolean> assertFailed = new ThreadLocal<Boolean>();
	
	private TestAssertions() {
	}
	
	
	@Step("Assert true - value:{condition}, message: {description}")
	public static void assertTrue(boolean condition, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		if (condition)                         
			TestReporter.logPass(ASSERT_TRUE, description);
		else {
			TestReporter.logFailure(ASSERT_TRUE, description);
			
			Assert.fail(ASSERT_TRUE + " - " + description);
		}
	}

	@Step("Assert false - value:{condition}, message: {description}")
	public static void assertFalse(boolean condition, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		if (!condition) 
			TestReporter.logPass(ASSERT_FALSE, description);
		else {
			TestReporter.logFailure(ASSERT_FALSE, description);
			
			Assert.fail(ASSERT_FALSE + " - " + description);
		}
	}

	@Step("Assert equals - value 1:{value1}, value 2: {value2}, message: {description}")
	public static void assertEquals(Object value1, Object value2, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		if (areEqual(value1, value2)) 
			TestReporter.logPass(ASSERT_EQUALS, description);            		
		else {
			TestReporter.logFailure(ASSERT_EQUALS, description);
			
			Assert.fail(ASSERT_EQUALS + " - " + description);
		}
	}

	@Step("Assert not equals - value 1:{value1}, value 2: {value2}, message: {description}")
	public static void assertNotEquals(Object value1, Object value2, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		if (!areEqual(value1, value2))
			TestReporter.logPass(ASSERT_NOT_EQUALS, description);                      
		else {
			TestReporter.logFailure(ASSERT_NOT_EQUALS, description);
			
			Assert.fail(ASSERT_NOT_EQUALS + " - " + description);
		}
	}

	@Step("Assert greater than zero - value:{value}")
	public static void assertGreaterThanZero(int value) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		final String description = value + " is greater than zero";

		if (value > 0)
			TestReporter.logPass(ASSERT_GREATER_THAN_0, description);            		     
		else {
			TestReporter.logFailure(ASSERT_GREATER_THAN_0, description);
			
			Assert.fail(ASSERT_GREATER_THAN_0 + " - " + description);
		}
	}

	@Step("Assert greater than zero - value:{value}")
	public static void assertGreaterThanZero(float value) {
		assertGreaterThanZero((int) value);
	}

	@Step("Assert greater than zero - value:{value}")
	public static void assertGreaterThanZero(double value) {
		assertGreaterThanZero((int) value);
	}

	@Step("Assert null - value:{value}, message: {description}")
	public static void assertNull(Object value, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		if (value == null) 
			TestReporter.logPass(ASSERT_NULL, description);            		    
		else {
			TestReporter.logFailure(ASSERT_NULL, description);
			
			Assert.fail(ASSERT_NULL + " - " + description);
		}
	}

	@Step("Assert not null - value:{value}, message: {description}")
	public static void assertNotNull(Object value, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		if (value != null)              
			TestReporter.logPass(ASSERT_NOT_NULL, description);
		else {
			TestReporter.logFailure(ASSERT_NOT_NULL, description);
			
			Assert.fail(ASSERT_NOT_NULL + " - " + description);
		}
	}

	@Step("Soft assert true - value:{condition}, message: {description}")
	public static boolean softAssertTrue(boolean condition, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		AssertionStatus status = condition ? PASS : FAIL;
		
		TestNgReporter.print(SOFT_ASSERT_TRUE , description, status);
		ConsoleReporter.print(SOFT_ASSERT_TRUE, description, status);

		if (!condition)
			assertFailed.set(true);

		return condition;
	}

	@Step("Soft assert equals value1: {value1}, value 2: {value2}, message: {description} ")
	public static boolean softAssertEquals(Object value1, Object value2, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		boolean areEqual = areEqual(value1, value2);
		AssertionStatus status = areEqual ? PASS : FAIL;
		
		TestNgReporter.print(SOFT_ASSERT_EQUALS , description, status);
		ConsoleReporter.print(SOFT_ASSERT_EQUALS, description, status);

		if (!areEqual)
			assertFailed.set(true);
		
		return areEqual;
	}

	private static boolean areEqual(Object expected, Object actual) {
		if((expected == null) && (actual == null)) 
			return false;

		if(expected == null ^ actual == null) 
			return false;

		return expected.equals(actual);
	}

	@Step("Soft assert false - value:{condition}, message: {description}")
	public static boolean softAssertFalse(boolean condition, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		AssertionStatus status = !condition ? PASS : FAIL;

		TestNgReporter.print(SOFT_ASSERT_FALSE , description, status);
		ConsoleReporter.print(SOFT_ASSERT_FALSE, description, status);

		if (condition)
			assertFailed.set(true);

		return !condition;
	}

	@Step("Soft assert null - value:{value}, message: {description}")
	public static boolean softAssertNull(Object value, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		AssertionStatus status = value == null ? PASS : FAIL;
		
		TestNgReporter.print(SOFT_ASSERT_NULL , description, status);
		ConsoleReporter.print(SOFT_ASSERT_NULL, description, status);

		if (value != null)
			assertFailed.set(true);

		return value == null;

	}

	@Step("Soft assert not null - value:{value}, message: {description}")
	public static boolean softAssertNotNull(Object value, String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		AssertionStatus status = value != null ? PASS : FAIL;
		
		TestNgReporter.print(SOFT_ASSERT_NOT_NULL , description, status);
		ConsoleReporter.print(SOFT_ASSERT_NOT_NULL, description, status);

		if (value == null) 
			assertFailed.set(true);

		return value != null;
	}

	@Step("Validate all soft assertions")
	public static void assertAll() {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		boolean failed = assertFailed.get() == null ? false : assertFailed.get();
		
		if (failed) {
			assertFailed.set(false);
			
			TestNgReporter.print("Soft assertions", "see failures above", FAIL);
			
			Assert.fail("Soft assertions failed - see testNG report for details");
		}
	}

	@Step("Assertion failure, message: {description}")
	public static void assertFail(String description) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		TestReporter.logFailure(ASSERT_FAIL, description);       	

		Assert.fail(description);
	}

}
