package com.framework.core.interfaces.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.framework.core.interfaces.Label;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import com.framework.utils.utilities.StackTraceInfo;

public class LabelImpl extends ElementImpl implements Label {

    public LabelImpl(AllegisDriver driver, By by) {
        super(driver, by);
    }

    public LabelImpl(AllegisDriver driver, By by, WebElement element) {
        super(driver, by, element);
    }

    @Override
    public String getFor() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getAttribute("for");
    }
}