package com.framework.utils.listeners;

import org.springframework.stereotype.Component;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * Remove the skips from the test invocation to prevent duplicate reporting of retries.
 *
 */
@Component
public class RetryListener implements IInvokedMethodListener {

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {		
		//no code needed
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		removeSkippedResult(testResult);
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		//no code needed
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult result, ITestContext context) {
        removeSkippedResult(result);
	}

	public void removeSkippedResult(ITestResult result) {
		if(result == null 
				|| result.getTestContext() == null 
				|| result.getTestContext().getSkippedTests() == null) {
			return;
		}
		
		result.getTestContext().getSkippedTests().removeResult(result.getMethod());
	}

}
