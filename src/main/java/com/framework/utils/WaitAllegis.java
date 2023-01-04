package com.framework.utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.framework.exception.AutomationException;
import com.framework.utils.utilities.StackTraceInfo;

import static com.framework.utils.Constants.*;

public class WaitAllegis {
	
	private WaitAllegis() {
		
	}
	
    public static void implicitWait(WebDriver driver, Duration duration) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        driver.manage().timeouts().implicitlyWait(duration);
    }
	
    public static void hardWait(long seconds) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	try {
    		Thread.sleep(seconds * 1000);
    	}
    	catch (InterruptedException e) {
    		//nothing
    	}
    }

    //waitUntilElementVisible
    public static void waitUntilElementVisible(WebDriver driver, WebElement element) {
        waitUntilElementVisible(driver, element, WEBDRIVER_TIMEOUT);
    }
    
    public static void waitUntilElementVisible(WebDriver driver, WebElement element, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	        
        wait(timeout, driver).untilElementIsDisplayed(element);
    }
    
    public static void waitUntilElementVisible(WebDriver driver, By by) {
    	waitUntilElementVisible(driver, by, WEBDRIVER_TIMEOUT);
    }
    
    public static void waitUntilElementVisible(WebDriver driver, By by, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(timeout, driver).untilElementIsDisplayed(by);
    }

    
    //waitUntilElementHidden
    public static void waitUntilElementHidden(WebDriver driver, WebElement element) {
        waitUntilElementHidden(driver, element, WEBDRIVER_TIMEOUT);
    }
    
    public static void waitUntilElementHidden(WebDriver driver, WebElement element, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(timeout, driver).untilElementHidden(element);
    }
    
    public static void waitUntilElementHidden(WebDriver driver, By by) {
    	waitUntilElementHidden(driver, by, WEBDRIVER_TIMEOUT);
    }

    public static void waitUntilElementHidden(WebDriver driver, By by, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(timeout, driver).untilElementHidden(by);
    }

    
    //waitUntilElementClickable
    public static void waitUntilElementClickable(WebDriver driver, WebElement element) {
    	waitUntilElementClickable(driver, element, WEBDRIVER_TIMEOUT);	
    }

    public static void waitUntilElementClickable(WebDriver driver, WebElement element, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(timeout, driver).untilElementIsClickable(element);
    }

    public static void waitUntilElementClickable(WebDriver driver, By by) {
    	waitUntilElementClickable(driver, by, WEBDRIVER_TIMEOUT);
    }

    public static void waitUntilElementClickable(WebDriver driver, By by, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(timeout, driver).untilElementIsClickable(by);
    }

    
    //waitUntilTitleIs
    public static void waitUntilTitleIs(WebDriver driver, String title) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(WEBDRIVER_TIMEOUT, driver).untilTitleIs(title);
    }
    
    
    //waitUntilTitleContains
    public static void waitUntilTitleContains(WebDriver driver, String title) {
    	waitUntilTitleContains(driver, title, WEBDRIVER_TIMEOUT);
    }
    
    public static void waitUntilTitleContains(WebDriver driver, String title, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(timeout, driver).untilTitleContains(title);
    }
    
    
    //waitUntilUrlIs
    public static void waitUntilUrlIs(WebDriver driver, String url) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(WEBDRIVER_TIMEOUT, driver).untilUrlIs(url);
    }
    
    
    //waitUntilUrlContains
    public static void waitUntilUrlContains(WebDriver driver, String url) {
    	waitUntilUrlContains(driver, url, WEBDRIVER_TIMEOUT);
    }
    
    public static void waitUntilUrlContains(WebDriver driver, String url, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(timeout, driver).untilUrlContains(url);
    }

    
    //waitUntilTextPresentInElement
    public static void waitUntilTextPresentInElement(WebDriver driver, WebElement element, String text) {
    	waitUntilTextPresentInElement(driver, element, text, WEBDRIVER_TIMEOUT);
    }
    
    public static void waitUntilTextPresentInElement(WebDriver driver, WebElement element, String text, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        wait(timeout, driver).untilTextPresentInElement(element, text);
    }
    
    private static AllegisWait wait(int timeout, WebDriver driver) {
    	
        return new AllegisWait.Builder()
								.withDriver(driver)
								.withTimeout(Duration.ofSeconds(timeout))
									.build();
        
    }

    @Deprecated
    public static void getReadyState(AllegisDriver driver, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        if (Boolean.FALSE.equals(isReadyStateComplete(driver, timeout)))
        	throw new AutomationException("ready state is not complete!");
    }

    @Deprecated
    public static Long measurePageLoadTime(AllegisDriver driver) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        Long loadTime = (Long) ((JavascriptExecutor) driver.getWebDriver()).executeScript(
                "return performance.timing.loadEventEnd - performance.timing.navigationStart;");

        if (Boolean.FALSE.equals(isReadyStateComplete(driver, ELEMENT_TIMEOUT)))
        	throw new AutomationException("ready state is not complete!");
        
        return loadTime;
    }
    
    @Deprecated
    public static boolean syncJS(AllegisDriver driver, int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
       	return isReadyStateComplete(driver, timeout);
    }    	
    
    private static Boolean isReadyStateComplete(AllegisDriver driver, int timeout) {
    	return driver.getReadyState(timeout).equalsIgnoreCase("complete");
    }
    
}