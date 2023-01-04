package com.framework.exception.automation;

import com.framework.exception.AutomationException;
import com.framework.utils.AllegisDriver;

public class OptionNotInListboxException extends AutomationException {

	private static final long serialVersionUID = 4926417034544326093L;

	public OptionNotInListboxException(String message) {
		super(message);
	}

	public OptionNotInListboxException(String message, AllegisDriver driver) {
		super(message, driver);
	}
}
