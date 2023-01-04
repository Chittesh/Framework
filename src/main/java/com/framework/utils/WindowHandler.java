package com.framework.utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.utils.utilities.StackTraceInfo;

public class WindowHandler {

	private WindowHandler() {
		
	}
	
	public static boolean waitUntilWindowExistsWithTitle(WebDriver driver, String windowName) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return waitUntilWindowExistsWithTitle(driver, windowName, Constants.PAGE_TIMEOUT);
	}

	public static boolean waitUntilWindowExistsWithTitle(WebDriver driver, String windowName, int timeout) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return wait(getWrappedDriver(driver), timeout)
				.until(ExtendedExpectedConditions.findWindowWithTitleAndSwitchToIt(windowName));
	}

	public static boolean waitUntilWindowExistsTitleContains(WebDriver driver, String windowName) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return waitUntilWindowExistsTitleContains(driver, windowName, Constants.PAGE_TIMEOUT);
	}

	public static boolean waitUntilWindowExistsTitleContains(WebDriver driver, String windowName, int timeout) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return wait(getWrappedDriver(driver), timeout)
				.until(ExtendedExpectedConditions.findWindowContainsTitleAndSwitchToIt(windowName));
	}

	public static boolean waitUntilWindowExistsTitleMatches(WebDriver driver, String regex) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return waitUntilWindowExistsTitleMatches(driver, regex, Constants.PAGE_TIMEOUT);
	}

	public static boolean waitUntilWindowExistsTitleMatches(WebDriver driver, String regex, int timeout) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return wait(getWrappedDriver(driver), timeout)
				.until(ExtendedExpectedConditions.findWindowMatchesTitleAndSwitchToIt(regex));
	}

	public static boolean waitUntilNumberOfWindowsAre(WebDriver driver, int windowCount) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return waitUntilNumberOfWindowsAre(driver, windowCount, Constants.PAGE_TIMEOUT);
	}

	public static boolean waitUntilNumberOfWindowsAre(WebDriver driver, int windowCount, int timeout) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return wait(getWrappedDriver(driver), timeout)
				.until(ExpectedConditions.numberOfWindowsToBe(windowCount));
	}
	
	private static WebDriverWait wait(WebDriver driver, int seconds) {
		return new WebDriverWait(driver, Duration.ofSeconds(seconds));
	}
	
	private static WebDriver getWrappedDriver(WebDriver driver) {
		return ((AllegisDriver)driver).getWebDriver();
	}
	
}