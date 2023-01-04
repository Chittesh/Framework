package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class ElementNotEnabledException extends AutomationException {

	private static final long serialVersionUID = 6579447002670243452L;

	public ElementNotEnabledException(String message) {
		super(message);
	}

	public ElementNotEnabledException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
