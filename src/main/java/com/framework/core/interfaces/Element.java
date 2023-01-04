
package com.framework.core.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.framework.core.Beta;
import com.framework.core.interfaces.impl.ElementImpl;
import com.framework.core.interfaces.impl.internal.ImplementedBy;

/**
 * wraps a web element interface with extra functionality. Anything added here
 * will be added to all descendants.
 */
@ImplementedBy(ElementImpl.class)
public interface Element extends WebElement, WrapsElement {

    public By getBy();

    public void setBy(By by);

    /**
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#clear()
     * @summary - used to clear text entry areas; has not effect on any other
     *          elements
     */
    @Override
    void clear();
    
    void uploadFile(String filePath);

    WebDriver getWrappedDriver();

    /**
     * @summary - draws the focus to an object using Actions
     */
    void focus();
    
    /**
     * @summary - draws the focus to an object using javascript
     */
    void jsFocus();

    /**
     * @summary - draws the focus on mouse hover event for an object using Actions
     */
    void focusOnHover();

    /**
     * @see org.openqa.selenium.old.WebElement#click()
     * @summary - default Selenium click
     */
    @Override
    void click();

    /**
     * @summary - click an element using a JavascriptExecutor
     * @param driver
     *            - Current active WebDriver object
     */
    void jsClick();
    
    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    void syncInFrame();

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)    
    void syncInFrame(int timeoutInSeconds);

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    void syncInFrame(int timeoutInSeconds, int timeoutInMilliseconds);

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)    
    boolean existsInFrame();

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)    
    boolean existsInFrame(int timeoutInSeconds);

    /**
     * @deprecated
     * This method is no longer acceptable for checking if an element is displayed
     * <p> Use {@link #isDisplayed(timeout) isDisplayed} method instead.     
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    boolean existsInFrame(int timeoutInSeconds, int timeoutInMilliseconds);

    /**
     * @summary - Click on an Element after synchronizing with that element even if
     *          it resides in a different frame. Keeps driver on that particular frame
     * @param none
     *            Uses default element timeout from constants class.
     */
    void clickOnSync();

    /**
     * @summary - Click on an Element after synchronizing with that element even if
     *          it resides in a different frame. Keeps driver on that particular frame.
     *          Will wait up to 'timeoutInSeconds' time in seconds.
     */
    void clickOnSync(int timeoutInSeconds);

    /**
     * @summary - Scroll the page to an Element after synchronizing with that element even if
     *          it resides in a different frame. Keeps driver on that particular frame
     * @param none
     *            Uses default element timeout from constants class.
     */
    void scrollOnSync();

    /**
     * @summary - Scroll the page to an Element after synchronizing with that element even if
     *          it resides in a different frame. Keeps driver on that particular frame.
     *          Will wait up to 'timeoutInSeconds' time in seconds.
     */
    void scrollOnSync(int timeoutInSeconds);

    /**
     * @summary - Scroll the page to an element and Click on same element after synchronizing with that element even if
     *          it resides in a different frame. Keeps driver on that particular frame
     * @param none
     *            Uses default element timeout from constants class.
     */
    void scrollToAndCickOnSync();

    /**
     * @summary - Scroll the page to an element and Click on same element after synchronizing with that element even if
     *          it resides in a different frame. Keeps driver on that particular frame.
     *          Will wait up to 'timeoutInSeconds' time in seconds.
     */
    void scrollToAndCickOnSync(int timeoutInSeconds);

    /**
     * @see org.openqa.selenium.old.WebElement#click()
     * @summary - draws the focus to an object and clicks the object using Actions
     * @param driver
     *            - Current active WebDriver object
     */
    void focusClick();

    /**
     * @param keysToSend
     *            - an array of characters or string literals
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#sendKeys(java.lang.CharSequence...)
     * @summary - sends the char sequence to the element if the sequnce is not an
     *          empty string
     */

    @Override
    void sendKeys(CharSequence... keysToSend);

    /**
     * @see org.openqa.selenium.old.WebElement#submit
     * @summary - submits form to remote server; exception thrown if the element is
     *          not within a form
     */
    @Override
    void submit();

    /**
     * @param by
     *            - Search for specified {@link By} location and return it's
     *            {@link WebElement}
     * @return {@link WebElement}
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#findElement()
     */
    @SuppressWarnings("unchecked")
    @Override
    WebElement findElement(By by);

    Element locateElement(By by);

    @SuppressWarnings({ "unchecked" })
    @Override
    List<WebElement> findElements(By by);

    List<Element> locateElements(By by);

    /**
     * @param name
     *            - Search for specified attribute and return it's value
     * @return {@link String} Value of specified attribute
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#getAttribute()
     */
    @Override
    String getAttribute(String name);

    @SuppressWarnings("rawtypes")
    ArrayList getAllAttributes();

    /**
     * @param propertyName
     *            - Search for specified property and return it's value
     * @return {@link String} Value of specified property
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#getCssValue()
     */
    @Override
    String getCssValue(String propertyName);

    /**
     * @return {@link Point} Return x and y location
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#getLocation()
     */
    @Override
    Point getLocation();

    /**
     * @return {@link Dimension} Return height and width of element
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#getSize()
     */
    @Override
    Dimension getSize();

    /**
     * @return {@link String} Text value in element
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#getText()
     */
    @Override
    String getText();

    /**
     * @return {@link String} Tag value in element
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#getTagName()
     */
    @Override
    String getTagName();

    /**
     * @return {@link Boolean} Return TRUE if element is enabled, FALSE if it is not
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#isEnabled()
     */
    @Override
    boolean isEnabled();
        
    boolean isEnabled(int timeout);

    @Override
    boolean isSelected();

    /**
     * @return {@link Boolean} Return TRUE if element is Displayed, FALSE if it is
     *         not
     * @see main.java.com.framework.core.interfaces.impl.ElementImpl#isDisplayed()
     */
    @Override
    boolean isDisplayed();
    
    boolean isDisplayed(int timeout);

    /**
     * @summary - Returns true when the inner element is ready to be used.
     * @return boolean true for an initialized WebElement, or false if we were
     *         somehow passed a null WebElement.
     */
    boolean elementWired();

    /**
     * @return {@link By} locator value of element that was used to create element
     *         using {@link FindBy}
     */
    String getElementIdentifier();

    /**
     * @return locator identifier and the value of element that was used to create
     *         element using {@link FindBy}
     */
    String getElementLocatorInfo();

    /**
     * @summary - Highlight an element on a page
     * @return NA
     */
    void highlight();

    /**
     * @summary - Clear the existing element highlight on a page
     * @return NA
     */
    void clearHighlight();

    /**
     * @summary - JavaScript blinking highlight on an element before performing an Action.
     *          - Reads properties from Config file
     *          - HIGHLIGHT
     *          - HIGHLIGHT_LOOP
     *          - HIGHLIGHT_COLOR
     * @return NA
     */
    void jsHighlight();

    /**
     * @summary - Used to highlight and element on a page
     * @param driver
     *            - Current active WebDriver object
     */
    public void scrollIntoView();

    @Beta
    @Override
    public <X> X getScreenshotAs(OutputType<X> target);

    /**
     * Used to determine if the desired element is visible on the screen Will wait
     * for default element timeout unless new timeout is passed in If object is not
     * visible within the time, handle the error based default handler or by boolean
     * passed in
     *
     * @param args
     *            Optional arguments </br>
     *            &nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in
     *            seconds the method should try to sync. Called with
     *            syncVisible(10)</br>
     *            &nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE,
     *            the test will throw an exception and fail the script. If FALSE,
     *            the script will not fail, instead a FALSE will be returned to the
     *            calling function. Called with syncVisible(10, false)
     */
    boolean syncVisible(Object... args);

    /**
     * Used to determine if the desired element is hidden on the screen Will wait
     * for default element timeout unless new timeout is passed in If object is not
     * hidden within the time, handle the error based default handler or by boolean
     * passed in
     *
     * @param args
     *            Optional arguments </br>
     *            &nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in
     *            seconds the method should try to sync. Called with
     *            syncHidden(10)</br>
     *            &nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE,
     *            the test will throw an exception and fail the script. If FALSE,
     *            the script will not fail, instead a FALSE will be returned to the
     *            calling function. Called with syncHidden(10, false)
     */
    boolean syncHidden(Object... args);

    /**
     * Used to determine if the desired element is enabled on the screen. Will wait
     * for default element timeout unless new timeout is passed in. If object is not
     * enabled within the time, handle the error based default handler or by boolean
     * passed in
     *
     * @param args
     *            Optional arguments </br>
     *            &nbsp;&nbsp;&nbsp;&nbsp;<b>timeout</b> - the maximum time in
     *            seconds the method should try to sync. Called with
     *            syncEnabled(10)</br>
     *            &nbsp;&nbsp;&nbsp;&nbsp;<b>failTestOnSyncFailure </b>- if TRUE,
     *            the test will throw an exception and fail the script. If FALSE,
     *            the script will not fail, instead a FALSE will be returned to the
     *            calling function. Called with syncEnabled(10, false)
     */
    boolean syncEnabled(Object... args);


    /**
     * @deprecated
     * This method is no longer supported.
     * Reloading a page until an element is displayed is a bad practice.
     */
    @Deprecated(since = "1.1.8.1-SNAPSHOT", forRemoval = true)
    void refreshDOMUntilElementExist(Integer... loopCount);
}