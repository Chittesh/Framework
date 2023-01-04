package com.framework.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.framework.core.interfaces.Element;
import com.framework.core.interfaces.impl.internal.ElementFactory;
import com.framework.exception.automation.PageInitialization;
import com.framework.utils.utilities.StackTraceInfo;

public class PageLoaded {
	
	private PageLoaded() {
		
	}

	@SuppressWarnings("rawtypes")
    public static boolean isElementLoaded(Class clazz, AllegisDriver oDriver, Element obj) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
        return isElementLoaded(clazz, oDriver, obj, oDriver.getElementTimeout());
    }

    @SuppressWarnings("rawtypes")
    public static boolean isElementLoaded(Class clazz, AllegisDriver oDriver, Element obj, int timeout) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
        int count = 0;
        int driverTimeout = oDriver.getElementTimeout();

        oDriver.setElementTimeout(1, TimeUnit.MILLISECONDS);

        try {
            while (!obj.elementWired()) {
                if (count == timeout) 
                    break;
                
                count++;
                initializePage(clazz, oDriver);
            }
        } catch (NullPointerException | NoSuchElementException | StaleElementReferenceException | PageInitialization e) {
            return false;
        } finally {
            oDriver.setElementTimeout(driverTimeout, TimeUnit.SECONDS);
        }

        return count < timeout;
    }

    public static boolean isDomComplete(AllegisDriver oDriver) {
        return isDomComplete(oDriver, oDriver.getPageTimeout());
    }

    public static boolean isDomComplete(AllegisDriver driver, int timeout) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		AllegisWait wait = new AllegisWait.Builder()
											.withDriver(driver)
											.withTimeout(Duration.ofSeconds(timeout))
											.build();
											
    	return (Boolean)wait.wrappedWait()
    							.until(
    								ExpectedConditions.jsReturnsValue("var result = document.readyState; return (result == 'complete');"));
    }

    public static void initializePage(Class<?> clazz, AllegisDriver oDriver) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
        try {
            ElementFactory.initElements(oDriver, clazz.getConstructor(TestEnvironment.class));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new PageInitialization("Unable to initialize page", oDriver);
        }
    }    	
    
}