package com.framework.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.framework.core.interfaces.Textbox;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import com.framework.utils.Base64Coder;
import com.framework.utils.utilities.StackTraceInfo;

public class TextboxImpl extends ElementImpl implements Textbox {

	private static final String SCROLL_JS    = "arguments[0].scrollIntoView(true)";
	private static final String CLICK_JS     = "arguments[0].click();";
	private static final String SET_VALUE_JS = "arguments[0].setAttribute('value', arguments[1])";	                                        
	
	public TextboxImpl(AllegisDriver driver, By by) {
        super(driver, by);
    }

    public TextboxImpl(AllegisDriver driver, By by, WebElement element) {
        super(driver, by, element);
    }

    @Override
    public void clear() {
        this.element.clear();
        
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }
    
    @Override
    public void sendKeys(CharSequence... keyword) {
        this.element.sendKeys(keyword);
        
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }
    
    @Override
    public void set(String text) {
        if (text.isEmpty()) 
        	return;
        
        clear();
        type(text);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void jsSet(String text) {
        if (text.isEmpty()) 
        	return;
        
        scrollJS();        
        setValueJS(text);        
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void scrollAndSet(String text) {        
        scrollJS();
        clickJS();
            
        set(text);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void safeSet(String text) {
        if (text.isEmpty()) 
        	return;
        
        clearJS();
        type(text);
        pressTab();
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void setSecure(String text) {
        set(decodeString(text));
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void safeSetSecure(String text) {
        safeSet(decodeString(text));
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public String getText() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return this.element.getAttribute("value");
    }
    
    public void clearJS() {
        setValueJS("");
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }
    
    public void scrollJS() {
    	this.driver.executeScript(SCROLL_JS, this.element);
    	
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }
    
    public void clickJS() {
    	this.driver.executeScript(CLICK_JS, this.element);
    	
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }
    
    public void setValueJS(String text) {
    	this.driver.executeScript(SET_VALUE_JS, this.element, text);
    	
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }
    
    public void type(String text) {
    	this.element.sendKeys(text);
    	
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }
    
    public void pressTab() {
    	this.element.sendKeys(Keys.TAB);
    	
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    private String decodeString(String text) {
		return Base64Coder.decodeString(text);
	}
    
}