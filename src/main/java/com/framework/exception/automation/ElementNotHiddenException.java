package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class ElementNotHiddenException extends AutomationException {

	private static final long serialVersionUID = 1865273000586352087L;

	public ElementNotHiddenException(String message) {
		super(message);
	}

	public ElementNotHiddenException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
