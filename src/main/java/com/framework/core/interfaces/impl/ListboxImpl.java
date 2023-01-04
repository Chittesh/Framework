package com.framework.core.interfaces.impl;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.framework.core.interfaces.Listbox;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import com.framework.utils.utilities.StackTraceInfo;

public class ListboxImpl extends ElementImpl implements Listbox {
    
	private Select list;

    public ListboxImpl(AllegisDriver driver, By by) {
        super(driver, by);
        this.list = new Select(getWrappedElement());
    }

    @Override
    public void select(String text) {
        list.selectByVisibleText(text);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void selectValue(String value) {
        list.selectByValue(value);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void deselectAll() {
        list.deselectAll();
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public List<WebElement> getOptions() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return list.getOptions();
    }

    @Override
    public void deselectByVisibleText(String text) {
        list.deselectByVisibleText(text);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void selectDropdownOptionByVisibleText(String visibleText) {        
        list.selectByVisibleText(visibleText);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public WebElement getFirstSelectedOption() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return list.getFirstSelectedOption();
    }

    @Override
    public boolean isSelected(String value) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        for (WebElement option : list.getAllSelectedOptions()) {
            if (option.getText().equals(value)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public List<WebElement> getAllSelectedOptions() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return list.getAllSelectedOptions();
    }

    @Override
    public boolean isMultiple() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return list.isMultiple();
    }

}