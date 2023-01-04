package com.framework.exception.automation;

import com.framework.exception.AutomationException;

public class KeyExistsException extends AutomationException {

	private static final long serialVersionUID = 8734638785785664287L;

	public KeyExistsException(String message) {
		super(message);
	}
}
