package com.framework.core.interfaces.impl;

import static java.lang.String.format;
import static java.util.stream.Collectors.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.core.Beta;
import com.framework.core.interfaces.Element;
import com.framework.exception.automation.ElementNotFoundInAnyFrameException;
import com.framework.utils.AllegisDriver;
import com.framework.utils.AuditLogger;
import static com.framework.utils.Constants.*;
import static com.framework.utils.ExtendedExpectedConditions.*;
import com.framework.utils.WaitAllegis;
import com.framework.utils.debugging.Highlight;
import com.framework.utils.utilities.StackTraceInfo;

public class ElementImpl implements Element {	

	private static final String ELEMENT_NOT_FOUND = "Element [%s] was not found in the page";
	protected WebElement element;
    protected By by;
    protected AllegisDriver driver;

    ResourceBundle configProp = ResourceBundle.getBundle(CONFIG_PATH);

    public ElementImpl(final AllegisDriver driver, final By by) {
        this.by = by;
        this.driver = driver;  
        
        this.element = findElementInDom(by);
    }

    public ElementImpl(final AllegisDriver driver, final By by, final WebElement element) {
        this.by = by;
        this.driver = driver;
        
        if (element == null)
        	throw new NotFoundException("element not found!");
        
        this.element = element;
    }   
    
    public ElementImpl(WebElement element) {
        if (element == null)
        	throw new NotFoundException("element not found!");
        
        this.element = element;
    }   
    
    @Override
	public void uploadFile(String filePath) {
		
		WebElement wrappedElement = getWrappedElement();
		WebDriver wrappedDriver   = driver.getWebDriver();

		if (wrappedDriver instanceof RemoteWebDriver &&
			!(wrappedDriver instanceof ChromeDriver) &&
			!(wrappedDriver instanceof FirefoxDriver))
			
			((RemoteWebElement)wrappedElement).setFileDetector(new LocalFileDetector());

		wrappedElement.sendKeys(filePath);
		
	}
    
    private WebElement findElementInDom(By by) {
    	 try {
         	return wait(5).until(presenceOfElementLocated(by));
         }
         catch (Exception e) {
             throw new NotFoundException(String.format(ELEMENT_NOT_FOUND, by.toString()));
         }
    }

    @Override
    public By getBy() {
        return by;
    }

    @Override
    public void setBy(By by) {
        this.by = by;
    }

    @Override
    public void click() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());    	
    	
        getWrappedElement().click();
    }

    @Override
    public void jsClick() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        getWrappedDriver().executeScript("arguments[0].scrollIntoView(true);arguments[0].click();",
                getWrappedElement());
    }

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    @Override
    public void syncInFrame() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        if (!findFrame(ELEMENT_TIMEOUT)) {
            throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
        }
    }

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)    
    @Override
    public void syncInFrame(int timeoutInSeconds) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        if (!findFrame(timeoutInSeconds)) {
            throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
        }
    }

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    @Override
    public void syncInFrame(int seconds, int milliseconds) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        double timeout = (double)seconds + (double)milliseconds/1000;
        
        if (!findFrame(timeout)) {
            throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
        }
    }

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    @Override
    public boolean existsInFrame(int timeoutInSeconds) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return this.findFrame(timeoutInSeconds);
    }

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    @Override
    public boolean existsInFrame() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return this.findFrame(ELEMENT_TIMEOUT);
    }

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    @Override
    public boolean existsInFrame(int seconds, int milliseconds) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        double timeout = (double)seconds + (double)milliseconds / 1000;
        return this.findFrame(timeout);
    }

    @Override
    public void clickOnSync() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	if (!this.isDisplayed(ELEMENT_TIMEOUT))
    		throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
    	
        this.syncEnabled();
        WaitAllegis.syncJS(getWrappedDriver(), ELEMENT_TIMEOUT);
        this.jsClick();
    }

    @Override
    public void clickOnSync(int timeoutInSeconds) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	if (!this.isDisplayed(timeoutInSeconds))
    		throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
    	
        this.syncEnabled();
        WaitAllegis.syncJS(getWrappedDriver(), timeoutInSeconds);
        this.jsClick();
    }

    @Override
    public void scrollOnSync() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	if (!this.isDisplayed(ELEMENT_TIMEOUT))
    		throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
    	
        this.syncEnabled();
        this.scrollIntoView();
    }

    @Override
    public void scrollOnSync(int timeoutInSeconds) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	if (!this.isDisplayed(timeoutInSeconds))
    		throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
    	
        this.syncEnabled();
        this.scrollIntoView();
    }

    @Override
    public void scrollToAndCickOnSync() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        if (!this.isDisplayed(ELEMENT_TIMEOUT))
        	throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
        
        this.syncEnabled();
        this.scrollIntoView();
        this.jsClick();
    }

    @Override
    public void scrollToAndCickOnSync(int timeoutInSeconds) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        if (!this.isDisplayed(timeoutInSeconds))
        	throw new ElementNotFoundInAnyFrameException(format(ELEMENT_NOT_FOUND, by));
        
        this.syncEnabled();
        this.scrollIntoView();
        this.jsClick();
    }

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link isDisplayed(timeout)} instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)    
    private boolean findFrame(double timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        try {
        	wait((int)timeout).until(ExpectedConditions.visibilityOfElementLocated(by));
        	return true;
        }
        catch (Exception e) {
        	return false;
        }
    }

    @Override
    public boolean isDisplayed() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().isDisplayed();
    }
    
    public boolean isDisplayed(int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        try {
        	wait(timeout).until(ExpectedConditions.visibilityOfElementLocated(getBy()));
        	return true;
        }
        catch (Exception e) {
        	return false;
        }
    }
    
    @Override
    public void focus() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        new Actions(driver).moveToElement(getWrappedElement()).perform();
    }

    @Override
    public void focusOnHover() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	new Actions(driver).moveToElement(this.element).perform();
    }
    
    public void jsFocus() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	getWrappedDriver().executeScript("arguments[0].scrollIntoView(true);arguments[0].focus();",
    			getWrappedElement());
    }

    @Override
    public void focusClick() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        new Actions(driver).moveToElement(getWrappedElement()).click().perform();
    }

    @Override
    public Point getLocation() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getLocation();
    }

    @Override
    public void submit() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        getWrappedElement().submit();
    }

    @Deprecated
    @Override
    public String getAttribute(String name) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getAttribute(name);
    }
    
    @Override
    public String getDomAttribute(String name) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getDomAttribute(name);
    }
    
    @Override
    public String getDomProperty(String name) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getDomProperty(name);
    }

    @Override
    public String getCssValue(String propertyName) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getCssValue(propertyName);
    }

    @Override
    public Dimension getSize() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getSize();
    }

    @Override
    public String getText() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getText();
    }

    @Override
    public String getTagName() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().getTagName();
    }
    
    @Override
    public List<Element> locateElements(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().findElements(by).stream()
        			.map(el -> new ElementImpl(driver, by, el))
        			.collect(toList());
    }

    @Override
    public List<WebElement> findElements(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().findElements(by);
    }

    
    @Override
    public Element locateElement(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new ElementImpl(this.driver, by);
    }

    @Override
    public WebElement findElement(By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().findElement(by);
    }

    @Override
    public boolean isEnabled() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().isEnabled();
    }
    
    public boolean isEnabled(int timeout) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        try {
        	wait(timeout).until(ExpectedConditions.elementToBeClickable(getBy()));
        	return true;
        }
        catch (Exception e) {
        	return false;
        }
    }
    
    @Override
    public boolean isSelected() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return getWrappedElement().isSelected();
    }

    @Override
    public void clear() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());    	    
    	
        getWrappedElement().clear();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());    	    	
    	
        getWrappedElement().sendKeys(keysToSend);
    }

    //@Override
    public WebElement getWrappedElement() {    	    		
    	return this.element;
    }

    @Override
    public AllegisDriver getWrappedDriver() {
        return driver;
    }

    @Override
    public boolean elementWired() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return (getWrappedElement() != null);
    }

    @Override
    public String getElementIdentifier() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	String byText = by.toString();
    	
        int startPosition = byText.lastIndexOf(": ") + 2;
        String locator = byText.substring(startPosition, byText.length());
        
        return locator.trim();
    }

    @Override
    public String getElementLocatorInfo() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return by.toString();
    }

    @Override
    public void highlight() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        Highlight.highlight(this.driver, this.element);
    }

    @Override
    public void clearHighlight() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        Highlight.clearHighlight(this.driver, this.element);
    }

    @Override
    public void jsHighlight() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        String color = configProp.getString("ELEMENT_HIGHLIGHT_COLOR");
        
        if (Boolean.valueOf(configProp.getString("ELEMENT_HIGHLIGHT"))) {
            for (int i = 0; i < Integer.parseInt(configProp.getString("ELEMENT_HIGHLIGHT_LOOP")); i++) {
                this.driver.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: " + color + "; border: 5px solid " + color + ";");
                this.driver.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
            }
        }
    }

    @Override
    public void scrollIntoView() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        getWrappedDriver().executeScript("arguments[0].scrollIntoView(true);", element);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ArrayList getAllAttributes() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return (ArrayList) driver.executeScript(
                "var s = []; var attrs = arguments[0].attributes; for (var l = 0; l < attrs.length; ++l) { var a = attrs[l]; s.push(a.name + ':' + a.value); } ; return s;",
                getWrappedElement());
    }

    @Beta
    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return ((TakesScreenshot) driver.getWebDriver()).getScreenshotAs(target);
    }
    
    private WebDriverWait wait(int seconds) {
    	return new WebDriverWait(this.driver.getWebDriver(), Duration.ofSeconds(seconds));
    }

    @Override
    public boolean syncVisible(Object... args) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return wait(getTimeout(args)).until(elementToBeVisible(this.element));
        
    }

    @Override
    public boolean syncHidden(Object... args) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return wait(getTimeout(args)).until(invisibilityOfElementLocated(by));

    }

    @Override
    public boolean syncEnabled(Object... args) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	int timeout = args.length > 0 ? Integer.valueOf(args[0].toString()) : ELEMENT_TIMEOUT;

        wait(timeout).until(elementToBeClickable(by));

        return true;
    }

    private int getTimeoutFromArguments(Object... args) {
    	 return args.length > 0 ? Integer.valueOf(args[0].toString()) : 0;
    }
    
    private int getTimeout(Object... args) {
    	return args.length == 0 ? this.driver.getElementTimeout() 
    						    : getTimeoutFromArguments(args);
    }
    
    /**
     * @deprecated
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    @Override
    public void refreshDOMUntilElementExist(Integer... loopCount) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        Integer iCount = loopCount.length > 0 ? loopCount[0] : LOOP_COUNT;
        for (int i = 0; i < iCount; i++) {
                if (this.existsInFrame(ELEMENT_TIMEOUT)) {
                    break;
                } else {
                    String url = getWrappedDriver().getCurrentUrl();
                    getWrappedDriver().navigate().refresh();
                    getWrappedDriver().get(url);
                }
        }
    }

    @Override
    public Rectangle getRect() {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return null;
    }

}