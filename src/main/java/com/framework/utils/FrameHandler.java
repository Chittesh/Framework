package com.framework.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.utils.utilities.StackTraceInfo;

public class FrameHandler {

	private FrameHandler() {
		
	}
	
	@Deprecated
	public static void findAndSwitchToFrame(WebDriver driver, String frame) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		driver.switchTo().defaultContent();
		
		getWait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
	}

	public static String getCurrentFrameName(WebDriver driver) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		WebDriver wrappedDriver = ((AllegisDriver) driver).getWebDriver();

		String frameName = ((JavascriptExecutor) wrappedDriver).executeScript("return self.name").toString();
		
		return frameName.isEmpty() ? null : frameName;
	}

	public static void moveToDefaultContext(WebDriver driver) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		driver.switchTo().defaultContent();
	}

	public static void moveToParentFrame(WebDriver driver) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		driver.switchTo().parentFrame();
	}

	public static void moveToSiblingFrame(WebDriver driver, String frameName) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		moveToParentFrame(driver);
		switchToFrame(driver, frameName);
	}

	public static void moveToSiblingFrame(WebDriver driver, By byFrameLocator) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		moveToParentFrame(driver);
		switchToFrame(driver, byFrameLocator);
	}

	public static void moveToChildFrame(WebDriver driver, String frameName) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		switchToFrame(driver, frameName);
	}

	public static void moveToChildFrame(WebDriver driver, By byFrameLocator) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		switchToFrame(driver, byFrameLocator);
	}

	public static void moveToChildFrame(WebDriver driver, String[] frameName) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		for (int x = 0; x < frameName.length; x++) {
			moveToChildFrame(driver, frameName[x]);
		}
	}

	public static void moveToChildFrame(WebDriver driver, By[] frameName) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		for (int x = 0; x < frameName.length; x++) {
			moveToChildFrame(driver, frameName[x]);
		}
	}

	private static void switchToFrame(WebDriver driver, String frameName) {
		getWait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
	}

	private static void switchToFrame(WebDriver driver, By byFrameLocator) {
		getWait(driver).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(byFrameLocator));
	}
	
	private static WebDriverWait getWait(WebDriver driver) {
		int timeout = ((AllegisDriver) driver).getElementTimeout();

		return new WebDriverWait(driver, Duration.ofSeconds(timeout));

	}		
	
}