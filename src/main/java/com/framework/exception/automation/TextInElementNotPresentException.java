package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class TextInElementNotPresentException extends AutomationException {
	private static final long serialVersionUID = 3407361723082329697L;

	public TextInElementNotPresentException(String message) {
		super(message);
	}

	public TextInElementNotPresentException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
