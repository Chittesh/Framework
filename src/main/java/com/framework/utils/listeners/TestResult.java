package com.framework.utils.listeners;

import org.testng.*;
import com.framework.utils.JavaUtilities;

public class TestResult implements IReporter {
	
	private static final String INVALID_TEST_STATUS  = "invalid test status!";
	private static final String STRING_FORMATTER     = "%.3f sec";
	
	private ITestResult result;
	
	public TestResult(ITestResult testResult) {
		this.result = testResult;
	}
	
	public String getName() {
		return result.getName();
	}

	public String failureReason() {
		Throwable throwable = this.result.getThrowable();
		
		return JavaUtilities.isValid(throwable) ? throwable.getMessage() : "NA";
	}
	
	public String getExecutionStatus() {
		String executionStatus = "";
		
		switch (this.result.getStatus()) {
			case ITestResult.FAILURE:
				executionStatus = "FAILED";
				break;

			case ITestResult.SUCCESS:
				executionStatus = "PASSED";
				break;

			case ITestResult.SKIP:
				executionStatus = "SKIPPED";
				break;

			default:
				throw new IllegalStateException(INVALID_TEST_STATUS);
		}
		
		return executionStatus;
	}

	public String getState() {
		String state = "";
		
		switch (this.result.getStatus()) {
			case ITestResult.FAILURE:
				state = "danger";
				break;

			case ITestResult.SUCCESS:
				state = "success";
				break;

			case ITestResult.SKIP:
				state = "warning";
				break;

			default:
				throw new IllegalStateException(INVALID_TEST_STATUS);
		}
		
		return state;
	}

	public String getFailureReason() {
		String reason = "";
		
		switch (this.result.getStatus()) {
			case ITestResult.FAILURE:
				reason = failureReason();
				break;

			case ITestResult.SUCCESS:
				reason = "N/A";
				break;

			case ITestResult.SKIP:
				reason = "N/A";
				break;

			default:
				throw new IllegalStateException(INVALID_TEST_STATUS);
		}
		
		return reason;
	}
	
	public String getTotalDuration() {
		String duration = "";		
		
		switch (this.result.getStatus()) {
			case ITestResult.FAILURE:
			case ITestResult.SUCCESS:
				duration = String.format(STRING_FORMATTER, getTotalTime());
				break;

			case ITestResult.SKIP:
				duration = "N/A";
				break;

			default:
				throw new IllegalStateException(INVALID_TEST_STATUS);
		}
		
		return duration;
	}
	
	public String getTestClassName() {
		return this.result.getTestClass().getName();
	}

	public String getTestMethodName() {
		return this.result.getName();
	}
	
	public float getTotalTime() {
		return ((float)this.result.getEndMillis() - this.result.getStartMillis())/1000;
	}
	
}