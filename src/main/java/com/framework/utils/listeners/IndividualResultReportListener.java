package com.framework.utils.listeners;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.internal.IResultListener2;

import com.framework.utils.Preamble;
import com.framework.utils.annotationsAllegis.Retry;
import com.framework.utils.annotationsAllegis.RetryAnalyzer;
import com.framework.utils.utilities.AnnotationInspector;
import com.framework.utils.utilities.RtNumberExtractor;

@Service
public abstract class IndividualResultReportListener implements IInvokedMethodListener, IResultListener2 {

	private static final String DEFAULT_TEMPLATE = "\n > > TestRun Details :> CLASS= '%s' :> METHOD= '%s' :> DESCRIPTION= '%s'";
	private static final String ASSERT_MESSAGE_TEMPLATE = " :> ASSERT_MESSAGE= '%s'";
	private static final String STACK_TRACE_TEMPLATE = " :> STACKTRACE= '%s'";
	
	private static final Logger LOGGER = LogManager.getLogger(IndividualResultReportListener.class);
	
	private AnnotationInspector annotation;
	private RtNumberExtractor rtExtractor;
	private Configuration config;

	@Inject
	public IndividualResultReportListener(Configuration config, AnnotationInspector annotation, RtNumberExtractor rtExtractor) {
		this.config = config;
		this.annotation = annotation;
		this.rtExtractor = rtExtractor;
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {

		if( testResult.getMethod().isTest()) {						
													
			rtExtractor		
				.extractNumbers(testResult)
				.forEach(vOneId -> report(testResult, vOneId));
			
		}
	}

	protected void report(ITestResult testResult, String... ids) {
		report("Executed in " + valueOf(getExecutionTime(testResult)) + " seconds." + createRetryMessage(testResult)
				+ getTestDetails(testResult), testResult, ids);
	}

	protected abstract void report(String message, ITestResult testResult, String... ids);

	protected String createRetryMessage(ITestResult testResult) {
		IRetryAnalyzer analyzer = testResult.getMethod().getRetryAnalyzer(testResult);

		if (analyzer == null || !RetryAnalyzer.class.isInstance(analyzer) || testResult.isSuccess()) {
			return "";
		}

		return annotation.retryFrom(testResult).map(Retry::value)
				.map(count -> format(" Retrying with @Retry annotation count = %s/%s",
						((RetryAnalyzer) analyzer).getCounter(), count))
				.orElse("");

	}

	protected String getTestDetails(ITestResult testResult) {
		ITestNGMethod testMethod = testResult.getMethod();
		StringBuilder message = new StringBuilder(format(DEFAULT_TEMPLATE, testMethod.getTestClass().getName(),
				testMethod.getMethodName(), testMethod.getDescription()));

		if (hasThrownMessage(testResult)) {
			message.append(format(ASSERT_MESSAGE_TEMPLATE, testResult.getThrowable().getMessage()));
		}

		if (isStackTraceEnabled()) {
			message.append(format(STACK_TRACE_TEMPLATE, getStackTrace(testResult)));
		}

		return message.toString();
	}

	private boolean hasThrownMessage(ITestResult testResult) {
		return testResult.getThrowable() != null && isNotBlank(testResult.getThrowable().getMessage());
	}

	private boolean isStackTraceEnabled() {
		return config.getBoolean("STACKTRACE_LOGGING_TO_VERSIONONE");
	}

	protected Double getExecutionTime(ITestResult testResult) {
		Long executionTimeinMiliseconds = (testResult.getEndMillis() - testResult.getStartMillis());
		Double executionTimeinSeconds = executionTimeinMiliseconds.doubleValue() / 1000;
		return Math.round(executionTimeinSeconds * 100.00) / 100.00;
	}

	private String getStackTrace(ITestResult testResult) {
		if (testResult.getThrowable() == null) {
			return "";
		}

		try (Stream<StackTraceElement> stream = Arrays.stream(testResult.getThrowable().getStackTrace())) {
			return stream.map(StackTraceElement::toString).collect(joining("\n"));
		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {
	}

	@Override
	public void onTestFailure(ITestResult result) {
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		if (wasNotSkippedForDependencyIssues(result)) {
			return;
		}

		annotation.preambleFrom(result).map(Preamble::versionOneTestID).filter(ids -> !"N/A".equals(ids[0]))
				.ifPresent(ids -> report(createSkipMessage(result), result, ids));
	}

	private String createSkipMessage(ITestResult result) {
		return "Test was skipped as it depends on failed/skipped test methods." + getTestDetails(result);
	}

	private boolean wasNotSkippedForDependencyIssues(ITestResult result) {
		return !hasThrownMessage(result)
				|| !result.getThrowable().getMessage().contains("depends on not successfully finished methods");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// The reports inheriting from this class need no implementation for this method.
	}

	@Override
	public void onStart(ITestContext context) {
		// The reports inheriting from this class need no implementation for this method.
	}

	@Override
	public void onFinish(ITestContext context) {
		// The reports inheriting from this class need no implementation for this method.
	}

	@Override
	public void onConfigurationSuccess(ITestResult itr) {
		// The reports inheriting from this class need no implementation for this method.
	}

	@Override
	public void onConfigurationFailure(ITestResult itr) {
		// The reports inheriting from this class need no implementation for this method.
	}

	@Override
	public void onConfigurationSkip(ITestResult itr) {
		// The reports inheriting from this class need no implementation for this method.
	}

	@Override
	public void beforeConfiguration(ITestResult tr) {
		// The reports inheriting from this class need no implementation for this method.
	}

	@Override
	public void onTestStart(ITestResult result) {
		// The reports inheriting from this class need no implementation for this method.
	}

}