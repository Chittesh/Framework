package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class ElementCssValueNotMatchingException extends AutomationException {
	private static final long serialVersionUID = 3407361723082329697L;

	public ElementCssValueNotMatchingException(String message) {
		super(message);
	}

	public ElementCssValueNotMatchingException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
