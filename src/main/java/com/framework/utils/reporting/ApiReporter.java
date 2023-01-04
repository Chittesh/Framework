package com.framework.utils.reporting;

import org.testng.Reporter;

import com.framework.api.restServices.core.RestResponse;
import com.framework.api.restServices.exceptions.RestException;
import com.framework.api.soapServices.core.SoapService;
import com.framework.api.soapServices.core.exceptions.SoapException;
import com.framework.utils.AuditLogger;
import com.framework.utils.TestReporter;
import com.framework.utils.utilities.StackTraceInfo;

public class ApiReporter {
	
	private static final String NEW_LINE = "<br/>";

	private ApiReporter() {		
	}

	public static void logAPI(boolean pass, String message, SoapService bs) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		String failFormat = "";
		if (!pass) {
			failFormat = "<font size = 2 color=\"red\">";
			TestReporter.logFailure(message);
		}
		String request = bs.getRequest().replaceAll("</*>", "</*>");
		String response = bs.getResponse();
		Reporter.log("<font size = 2><b>Endpoint: " + bs.getServiceURL() + "</b></font><br/>" + failFormat + "<b><br/> SOAP REQUEST [ " + bs.getServiceName() + "#" + bs.getOperationName() + " ] </b></font>");
		Reporter.setEscapeHtml(true);
		Reporter.log(request);
		Reporter.setEscapeHtml(false);
		Reporter.log("<br/><br/>");
		Reporter.log(failFormat + "<b> SOAP RESPONSE [ " + bs.getServiceName() + "#" + bs.getOperationName() + " ] </b></font>");
		Reporter.setEscapeHtml(true);
		Reporter.log(response);
		Reporter.setEscapeHtml(false);
		Reporter.log(NEW_LINE);

		if (!pass) {
			throw new SoapException(message);
		}
	}

	public static void logAPI(boolean pass, String message, RestResponse rs) {
		AuditLogger.collect(StackTraceInfo.getMethodInfo());

		String failFormat = "";
		if (!pass) {
			failFormat = "<font size = 2 color=\"red\">";
			TestReporter.logFailure(message);
		}

		Reporter.log("<font size = 2><b>Endpoint: " + rs.getMethod() + " " + rs.getURL() + "</b><br/>" + failFormat + "<b>REST REQUEST </b></font>");
		Reporter.setEscapeHtml(true);
		Reporter.log(rs.getRequestBody().replaceAll("</*>", "</*>"));
		Reporter.setEscapeHtml(false);
		Reporter.log(NEW_LINE);
		Reporter.log(failFormat + "<br/><b>REST RESPONSE</b></font>");
		Reporter.setEscapeHtml(true);
		Reporter.log(rs.getResponse());
		Reporter.setEscapeHtml(false);
		Reporter.log(NEW_LINE);

		if (!pass) 
			throw new RestException(message);
	}
	
	public static void logNoHtmlTrim(String message) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	TestReporter.log(message);
    }

    public static void logNoXmlTrim(String message) {
    	AuditLogger.collect(StackTraceInfo.getMethodInfo());
    	
    	Reporter.setEscapeHtml(true);
        Reporter.log("");
        Reporter.log(message);
        Reporter.setEscapeHtml(false);
        Reporter.log("<br /");

        ConsoleReporter.printInfoText(message);
    }


}
