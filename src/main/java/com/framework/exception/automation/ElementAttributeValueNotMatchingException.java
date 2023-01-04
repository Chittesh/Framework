package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class ElementAttributeValueNotMatchingException extends AutomationException {
	private static final long serialVersionUID = 3407361723082329697L;

	public ElementAttributeValueNotMatchingException(String message) {
		super(message);
	}

	public ElementAttributeValueNotMatchingException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
