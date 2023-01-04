package com.framework.utils.utilities;

import java.util.stream.Stream;

import org.testng.ITestResult;

public interface RtNumberExtractor {

	Stream<String> extractNumbers(ITestResult testResult);

	boolean idsNotPresent(ITestResult testResult);

	boolean idsPresent(ITestResult testResult);

	String getMissingMessage();

}