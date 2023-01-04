package com.framework.utils.listeners;

import java.util.Collection;

/**
 * Indicates that a given ID was not valid for Version One. 
 */
public class InvalidVersionOnIdException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidVersionOnIdException(Collection<String> id) {
		super(String.format("Some Version One ID were incorrect. ids: %s ", id));
	}

}
