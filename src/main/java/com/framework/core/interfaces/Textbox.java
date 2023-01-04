package com.framework.core.interfaces;

import com.framework.core.interfaces.impl.TextboxImpl;
import com.framework.core.interfaces.impl.internal.ImplementedBy;

/**
 * Text field functionality.
 */
@ImplementedBy(TextboxImpl.class)
public interface Textbox extends Element {
	/**
	 * @see org.openqa.selenium.old.WebElement#clear()
	 */
	@Override
	public void clear();
	
	@Override
	public void sendKeys(CharSequence... keyword);

	/**
	 * @param text
	 *            - The text to type into the field.
	 */
	void set(String text);

	/**
	 * @param text
	 *            - The text to type into the field.
	 */
	void scrollAndSet(String text);

	/**
	 * @param text
	 *            - The text to type into the field.
	 */
	void safeSet(String text);

	/**
	 * @param text
	 *            - Encoded text to decode then type in the field
	 */
	void setSecure(String text);

	/**
	 * @param text
	 *            - The text to type into the field.
	 */
	void safeSetSecure(String text);

	/**
	 * @see org.openqa.selenium.old.WebElement#getText()
	 */
	@Override
	public String getText();

	void jsSet(String text);

}
