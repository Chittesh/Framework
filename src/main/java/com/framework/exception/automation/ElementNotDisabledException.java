package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class ElementNotDisabledException extends AutomationException {
	private static final long serialVersionUID = 624614577584686540L;

	public ElementNotDisabledException(String message) {
		super(message);
	}

	public ElementNotDisabledException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
