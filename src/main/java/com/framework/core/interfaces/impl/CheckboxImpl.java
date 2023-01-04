package com.framework.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.framework.core.interfaces.Checkbox;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import com.framework.utils.utilities.StackTraceInfo;

public class CheckboxImpl extends ElementImpl implements Checkbox {

	private static final String TOGGLE_JS = 
			"if( document.createEvent ) {var ev = document.createEvent('MouseEvents'); ev.initEvent('click', true , true )" +
            ";arguments[0].dispatchEvent(ev);} else { arguments[0].click();}";
	
    public CheckboxImpl(AllegisDriver driver, By by) {
        super(driver, by);
    }

    public CheckboxImpl(AllegisDriver driver, By by, WebElement element) {
        super(driver, by);
    }

    @Override
    public void toggle() {
        getWrappedElement().click();
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void jsToggle() {
        getWrappedDriver().executeScript(TOGGLE_JS, element);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void check() {
        if (!isChecked()) 
           toggle();
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void uncheck() {
        if (isChecked()) 
           toggle();
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public boolean isChecked() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return this.element.isSelected();
    }
}