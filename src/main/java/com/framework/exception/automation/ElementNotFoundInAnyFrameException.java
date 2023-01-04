package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class ElementNotFoundInAnyFrameException extends AutomationException {

	private static final long serialVersionUID = 1865273000586352087L;

	public ElementNotFoundInAnyFrameException(String message) {
		super(message);
	}

	public ElementNotFoundInAnyFrameException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
