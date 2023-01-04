package com.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.framework.utils.utilities.StackTraceInfo;

public class ExtendedExpectedConditions {

	private static final String TYPE = "type";
	private static final String HIDDEN = "hidden";

	private ExtendedExpectedConditions() {
		
	}
	
	public static ExpectedCondition<Boolean> findWindowWithTitleAndSwitchToIt(final String title) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());
		
		return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    for (String handle : driver.getWindowHandles()) {
                        driver.switchTo().window(handle);
                        if (driver.getTitle().equals(title)) {
                            return true;
                        }
                    }
                } catch (WebDriverException e) {
                    return false;
                }
                return false;
            }

            @Override
            public String toString() {
                return String.format("text ('%s') to be present in window", title);
            }
        };
    }

    public static ExpectedCondition<Boolean> findWindowContainsTitleAndSwitchToIt(final String title) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    for (String handle : driver.getWindowHandles()) {
                        driver.switchTo().window(handle);
                        if (driver.getTitle().contains(title)) {
                            return true;
                        }
                    }
                } catch (WebDriverException e) {
                    return false;
                }
                return false;
            }

            @Override
            public String toString() {
                return String.format("text ('%s') to be present in window", title);
            }
        };
    }

    public static ExpectedCondition<Boolean> findWindowMatchesTitleAndSwitchToIt(final String regex) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    for (String handle : driver.getWindowHandles()) {
                        driver.switchTo().window(handle);
                        if (driver.getTitle().matches(regex)) {
                            return true;
                        }
                    }
                } catch (WebDriverException e) {
                    return false;
                }
                return false;
            }

            @Override
            public String toString() {
                return String.format("regex ('%s') to be in window", regex);
            }
        };
    }

    public static ExpectedCondition<Boolean> textToBePresentInElementAttribute(final WebElement element, final String attributeName, final String text) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String attribute = element.getAttribute(attributeName);
                    
                    return attribute != null ? attribute.contains(text) : false;
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("text ('%s') to be the value attribute ('%s') in element %s", text, attributeName, element);
            }
        };
    }


    public static ExpectedCondition<Boolean> textToMatchInElementAttribute(final WebElement element, final String attribute, final String regex) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String elementText = element.getAttribute(attribute);

                    return elementText != null ? elementText.matches(regex) : false;
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("attribute ('%s') to  match regex pattern ('%s') in element %s", attribute, regex,
                        element);
            }
        };
    }


    public static ExpectedCondition<Boolean> textToBePresentInElementCssProperty(final WebElement element, final String cssProperty, final String text) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String elementText = element.getCssValue(cssProperty);

                    return elementText != null ? elementText.contains(text) : false;
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("value ('%s') to be the value CSS Property ('%s') in element %s", text,
                        cssProperty, element);
            }
        };
    }

    public static ExpectedCondition<Boolean> textToMatchInElementCssProperty(final WebElement element, final String cssProperty, final String regex) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String elementText = element.getCssValue(cssProperty);

                    return elementText != null ? elementText.matches(regex) : false;
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("value ('%s') to match regex pattern of value CSS Property ('%s') in element %s",
                        regex, cssProperty, element);
            }
        };
    }

    public static ExpectedCondition<Boolean> elementToBeVisible(final WebElement element) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    Point location = element.getLocation();
                    Dimension size = element.getSize();
                    
                    Boolean positiveCoordinates = location.getX() > 0 && location.getY() > 0;
                    Boolean positiveSize        = size.getHeight() > 0 && size.getWidth() > 0;
                    
                    if (Boolean.FALSE.equals(positiveCoordinates) || Boolean.FALSE.equals(positiveSize))
                    	return false;
                    	
                    if (Boolean.TRUE.equals(isHidden(element))) 
                        return false;

                    return true;
                } catch (WebDriverException | ClassCastException | NullPointerException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("element ('%s') to be visible", element);
            }
        };
    }
    
    private static Boolean isHidden(WebElement element) {
         return element.getAttribute(HIDDEN).equalsIgnoreCase("true") ||
                element.getAttribute(TYPE).equalsIgnoreCase(HIDDEN);
    }

    public static ExpectedCondition<Boolean> elementToFoundInFrame(final By by) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
            	return driver.findElement(by).isDisplayed();
            }
            
            @Override
            public String toString() {
                return "";
            }

        };
    }
    
}