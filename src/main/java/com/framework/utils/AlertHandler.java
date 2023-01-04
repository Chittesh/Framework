package com.framework.utils;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.utils.utilities.StackTraceInfo;

public class AlertHandler {
	
	private AlertHandler() {
		
	}
	
    public static boolean isAlertPresent(WebDriver driver, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	try {
            new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void handleAllAlerts(WebDriver driver, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        while (isAlertPresent(driver, timeout)) {
            alertHandler(driver);
        }
    }

    public static void handleAlert(WebDriver driver, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        if (isAlertPresent(driver, timeout)) {
            alertHandler(driver);
        }
    }

    public static void handleAlert(WebDriver driver, int timeout, String inputText) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        if (isAlertPresent(driver, timeout)) {
            alertHandler(driver, inputText);
        }
    }

    private static void alertHandler(WebDriver driver) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception throwAway) {
        	//nothing
        }
    }

    private static void alertHandler(WebDriver driver, String inputText) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(inputText);
            alertHandler(driver);
        } catch (Exception throwAway) {
        	//nothing
        }
    }	
    
}