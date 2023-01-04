package com.framework.api.soapServices.core.exceptions;

import com.framework.exception.AutomationException;

public class XPathNotFoundException extends AutomationException {
	private static final long serialVersionUID = 3407361723082329697L;

	public XPathNotFoundException(String message) {
		super("No xpath was found with the path [ " + message + " ] ");
	}
}