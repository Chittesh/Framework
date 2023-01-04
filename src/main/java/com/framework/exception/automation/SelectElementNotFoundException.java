package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class SelectElementNotFoundException extends AutomationException {
	private static final long serialVersionUID = 7724792038612608062L;

	public SelectElementNotFoundException(String message) {
		super(message);
	}

	public SelectElementNotFoundException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
