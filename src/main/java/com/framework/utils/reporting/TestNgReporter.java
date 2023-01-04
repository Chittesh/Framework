package com.framework.utils.reporting;

import org.testng.Reporter;

import com.framework.exception.AutomationException;
import com.framework.utils.assertions.AssertionStatus;
import static com.framework.utils.reporting.ReporterUtilities.*;
import static com.framework.utils.reporting.ReporterConstants.*;
import static com.framework.utils.assertions.AssertionStatus.*;

public class TestNgReporter {

	private TestNgReporter() {
	}	

	public static void print(String assertionName, String description, AssertionStatus status) {
		String rowTemplate = "";

		if (status == PASS)
			rowTemplate = GREEN_ROW;
		else
			if (status == FAIL)
				rowTemplate = RED_ROW;
			else
				throw new AutomationException("invalid status - " + status.toString());
		
		Reporter.log(String.format(rowTemplate, getTimestamp(), assertionName, description));
	}            

	public static void print(String message) {
		Reporter.log(getTimestamp() + message + "<br />");
	}            

}
