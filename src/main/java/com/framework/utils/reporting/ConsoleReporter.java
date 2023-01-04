package com.framework.utils.reporting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.framework.exception.AutomationException;
import com.framework.utils.assertions.AssertionStatus;

import static com.framework.utils.reporting.ReporterUtilities.*;

import static com.framework.utils.reporting.ReporterConstants.*;
import static com.framework.utils.assertions.AssertionStatus.*;

public class ConsoleReporter {

	private static boolean printEnabled = true;
	
	private static Logger logger = LogManager.getLogger(); 
	
	private ConsoleReporter() {
	}
	
	public static void enablePrinting() {
		printEnabled = true;
	}
	
	public static void disablePrinting() {
		printEnabled = false;
	}
	
	public static void printInfoText(String text) 	{		print(text, INFO);		}
	public static void printErrorText(String text)  {		print(text, ERROR);		}
	public static void printDebugText(String text)  {		print(text, DEBUG);		}
	public static void printTraceText(String text)  {		print(text, TRACE);		}

	public static void print(String text, int loggingLevel) {
		if (!printEnabled) 
			return;

		if (loggingLevel < INFO && loggingLevel > ERROR)
			throw new AutomationException("invalid logging error - " + loggingLevel);

		String template = "{} {}";
		
		String noHtmlText = trimHtml(text);
		
		if (loggingLevel == INFO)
			logger.info(template, getTimestamp(), noHtmlText);
		
		if (loggingLevel == DEBUG)
			logger.debug(template, getTimestamp(), noHtmlText);
		
		if (loggingLevel == TRACE)
			logger.trace(template, getTimestamp(), noHtmlText);
		
		if (loggingLevel == ERROR)
			logger.error(template, getTimestamp(), noHtmlText);
	}
	
	
	
	public static void print(String assertion, String description, AssertionStatus status) {
		if (!printEnabled) 
			return;

		String template = status == PASS ? PASSING_ROW : FAILING_ROW;							
		String text     = String.format(template, getTimestamp(), assertion, trimHtml(description));
		
		if (status == PASS)
			logger.info(text);
		else
			logger.error(text);
	}
	
}
