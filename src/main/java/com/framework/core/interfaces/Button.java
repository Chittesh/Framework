package com.framework.core.interfaces;

import com.framework.core.interfaces.impl.ButtonImpl;
import com.framework.core.interfaces.impl.internal.ImplementedBy;

/**
 * Interface that wraps a WebElement in Button functionality.
 */
@ImplementedBy(ButtonImpl.class)
public interface Button extends Element {
	/**
	 * @summary - Click the button using the default Selenium click
	 */
	@Override
	public void click();

	/**
	 * @summary - Click the button using a JavascriptExecutor click
	 * @param driver
	 *            - Current active WebDriver object
	 */
	@Override
	public void jsClick();

}