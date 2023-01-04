package com.framework.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.framework.core.interfaces.Button;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import com.framework.utils.utilities.StackTraceInfo;

public class ButtonImpl extends ElementImpl implements Button {

    public ButtonImpl(AllegisDriver driver, By by) {
        super(driver, by);
    }

    public ButtonImpl(AllegisDriver driver, By by, WebElement element) {
        super(driver, by, element);
    }

    @Override
    public void click() {
        this.element.click();
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }

    @Override
    public void jsClick() {
        getWrappedDriver().executeScript("arguments[0].click();", this.element);
        
        AuditLogger.collect(StackTraceInfo.getMethodInfo());
    }
}