package com.framework.utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class AllegisWait
{
	private Duration timeout; 
	private Duration pollingInterval; 
	
	private Class<? extends Throwable> exceptionType;
	
	private String timeoutMessage;
	
	private FluentWait<WebDriver> wrappedWait;

	private AllegisWait(WebDriver driver, 
						Duration timeout, 
						Duration polling, 
						Class<? extends Throwable> exceptionType, 
						String timeoutMessage) {

		this.timeout         = timeout.equals(Duration.ZERO) ? Duration.ofSeconds(30) : timeout;
		this.pollingInterval = polling.equals(Duration.ZERO) ? Duration.ofMillis(500) : polling;
		
		this.exceptionType   = exceptionType != null ? exceptionType : null;
		this.timeoutMessage  = timeoutMessage.isEmpty() ? "" : timeoutMessage;
		
		this.wrappedWait = new FluentWait<WebDriver>(driver)
									.withTimeout(this.timeout)
									.pollingEvery(this.pollingInterval);
		
		if (this.exceptionType != null)
			this.wrappedWait = this.wrappedWait.ignoring(this.exceptionType);
		
		if (!this.timeoutMessage.isEmpty())
			this.wrappedWait = this.wrappedWait.withMessage(this.timeoutMessage);
		
	}

	public Duration getTimeout() {
		return timeout;
	}

	public Duration getPollingInterval() {
		return pollingInterval;
	}

	public Class<? extends Throwable> getExceptionType() {
		return exceptionType;
	}

	public String getTimeoutMessage() {
		return timeoutMessage;
	}
	
	public Wait<WebDriver> wrappedWait() {
		return wrappedWait;
	}
	
	public WebElement untilElementIsClickable(By by) {
		return wrappedWait.until(ExpectedConditions.elementToBeClickable(by));
	}

	public WebElement untilElementIsClickable(WebElement element) {
		return wrappedWait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public WebElement untilElementIsDisplayed(By by) {
		return wrappedWait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public WebElement untilElementIsDisplayed(WebElement element) {
		return wrappedWait.until(ExpectedConditions.visibilityOf(element));
	}
	
    public Boolean untilElementHidden(WebElement element) {
	    return wrappedWait.until(ExpectedConditions.invisibilityOf(element));
	}
	    
    public Boolean untilElementHidden(By by) {
        return wrappedWait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	public Boolean untilTitleContains(String keyword) {
		return wrappedWait.until(ExpectedConditions.titleContains(keyword));
	}
	
	public Boolean untilTitleIs(String title) {
		return wrappedWait.until(ExpectedConditions.titleIs(title));
	}
	
	public Boolean untilUrlContains(String keyword) {
		return wrappedWait.until(ExpectedConditions.urlContains(keyword));
	}
	
	public Boolean untilUrlIs(String url) {
		return wrappedWait.until(ExpectedConditions.urlToBe(url));
	}

	public Boolean untilTextPresentInElement(WebElement element, String text) {
        return wrappedWait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}
	
	public static class Builder
	{
		private WebDriver driver;
		
		private Duration timeout         = Duration.ZERO; 
		private Duration pollingInterval = Duration.ZERO; 
		
		private Class<? extends Throwable> exceptionType = null;
		
		private String timeoutMessage = "";
		
		public Builder() {
			//empty constructor
		}
	
		public Builder withDriver(WebDriver driver) {
			this.driver = driver;
			return this;
		}
		
		public Builder withTimeout(Duration timeout) {
			this.timeout = timeout;
			return this;
		}
		
		public Builder withPollingInterval(Duration pollingInterval) {
			this.pollingInterval = pollingInterval;
			return this;
		}
		
		public Builder withExceptionType(Class<? extends Throwable> exceptionType) {
			this.exceptionType = exceptionType;
			return this;
		}
		
		public Builder withTimeoutMessage(String timeoutMessage) {
			this.timeoutMessage = timeoutMessage;
			return this;
		}
		
		public AllegisWait build() {
			AllegisWait wait = new AllegisWait(this.driver,
											   this.timeout, 
											   this.pollingInterval, 
											   this.exceptionType, 
											   this.timeoutMessage);
			
			resetState();
			
			return wait;
		}
		
		/*
		 * useful if the same builder object is used for constructing more than 1 wait
		 */
		private void resetState() {
			this.driver          = null;
			this.timeout         = Duration.ZERO;
			this.pollingInterval = Duration.ZERO;
			this.exceptionType   = null;
			this.timeoutMessage  = "";
		}
	}

}