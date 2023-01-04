package com.framework.utils.listeners;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener2;

import com.framework.utils.TestReporter;
import com.framework.utils.status.TestStatus;
import com.framework.utils.utilities.ResultStreamer;

/**
 * Locally report the total of tests failed, skipped and successful.
 */
@Component
public class LocalReportTestListener implements IResultListener2 {
	
	private static Map<TestStatus, String> messages = new HashMap<>();
	
	static {
		messages.put(TestStatus.FAILURE, "TestMethod '%s' >>> FAIL");
		messages.put(TestStatus.SUCCESS, "TestMethod '%s' >>> PASS");
		messages.put(TestStatus.SKIP, "TestMethod '%s' >>> SKIP");
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		//no code needed
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		//no code needed
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {
		//no code needed
	}

	@Override
	public void onTestStart(ITestResult result) {
		//no code needed
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		//no code needed
	}

	@Override
	public void onStart(ITestContext context) {
		//no code needed
	}

	@Override
	public void onFinish(ITestContext context) {
		
		endResults(context)
			.map(result -> format(messages.get(TestStatus.byCode(result.getStatus())), result.getMethod().getMethodName() ) )
			.forEach(message -> TestReporter.log(message) );

	}

	public Stream<ITestResult> endResults(ITestContext context) {
		return new ResultStreamer(context).getTestsFilteredOfRetries();
	}

	@Override
	public void onConfigurationSuccess(ITestResult itr) {
		//no code needed
	}

	@Override
	public void onConfigurationFailure(ITestResult itr) {
		//no code needed
	}

	@Override
	public void onConfigurationSkip(ITestResult itr) {
		//no code needed
	}

	@Override
	public void beforeConfiguration(ITestResult tr) {
		//no code needed
	}

}
