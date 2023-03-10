package com.framework.api.soapServices.core.exceptions;

import com.framework.api.WebServiceException;

public class SoapException extends WebServiceException {
	private static final long serialVersionUID = -8710980695994382082L;

	public SoapException() {
		super("SOAP Error:");
	}

	public SoapException(String message) {
		super("SOAP Error: " + message);
	}

	public SoapException(String message, Throwable cause) {
		super("SOAP Error: " + message, cause);
	}
}
