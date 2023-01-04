package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class ElementNotVisibleException extends AutomationException {
	private static final long serialVersionUID = 7724792038612608062L;

	public ElementNotVisibleException(String message) {
		super(message);
	}

	public ElementNotVisibleException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
