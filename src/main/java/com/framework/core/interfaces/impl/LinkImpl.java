package com.framework.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.framework.core.interfaces.Link;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import com.framework.utils.utilities.StackTraceInfo;

public class LinkImpl extends ElementImpl implements Link {

	private final static String CLICK_JS = 
			"if( document.createEvent ) {var click_ev = document.createEvent('MouseEvents'); click_ev.initEvent('click', true , true )" +
            ";arguments[0].dispatchEvent(click_ev);} else { arguments[0].click();}";
	
    public LinkImpl(AllegisDriver driver, By by) {
        super(driver, by);
    }

    public LinkImpl(AllegisDriver driver, By by, WebElement element) {
        super(driver, by, element);
    }

    @Override
    public void jsClick() {
        getWrappedDriver().executeScript(CLICK_JS, element);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void click() {
        getWrappedElement().click();
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public String getURL() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getAttribute("href");
    }
    
}
