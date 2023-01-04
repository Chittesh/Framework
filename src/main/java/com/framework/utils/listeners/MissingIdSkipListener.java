package com.framework.utils.listeners;

import static java.lang.String.format;

import javax.inject.Inject;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;

import com.framework.utils.TestReporter;
import com.framework.utils.status.TestStatus;
import com.framework.utils.utilities.AnnotationInspector;
import com.framework.utils.utilities.RtNumberExtractor;


/**
 * Look for methods that have a missing @Preamble and mark them as skipped so that they do not
 * get executed.
 */
public class MissingIdSkipListener implements IInvokedMethodListener {

	private AnnotationInspector annotation;
	private RtNumberExtractor rtExtractor;
	
	@Inject
	public MissingIdSkipListener(RtNumberExtractor rtExtractor, AnnotationInspector annotation) {
		this.rtExtractor = rtExtractor;
		this.annotation = annotation;
	}
	
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		skipMissingId(method, testResult);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// Nothing to do here.
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		skipMissingId(method, testResult);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		// Nothing to do here.
	}
	
	private void skipMissingId(IInvokedMethod method, ITestResult testResult) {
		if(	isTest(method) && rtExtractor.idsNotPresent(testResult) ) {
			testResult.setStatus(TestStatus.SKIP.getCode());
			testResult.setThrowable(new SkipException(rtExtractor.getMissingMessage()));
			TestReporter.logInfo(format("Test skipped due to missing Version One Id: %s", method));
		}
	}

	private boolean isTest(IInvokedMethod method) {
		return annotation.testFrom(method).isPresent();
	}
}
