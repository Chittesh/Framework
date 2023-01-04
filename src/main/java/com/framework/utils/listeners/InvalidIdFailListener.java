package com.framework.utils.listeners;

import static java.lang.String.format;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlException;
import org.springframework.stereotype.Service;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.framework.api.v1.ReadOnlyVersionOneServiceProvider;
import com.framework.api.v1.VersionOneService;
import com.framework.utils.TestReporter;
import com.framework.utils.status.TestStatus;
import com.framework.utils.utilities.AnnotationInspector;
import com.framework.utils.utilities.RtNumberExtractor;


/**
 * Verify the Version One ID for methods that are to be run and fail them if the ID
 * cannot be found in version one. 
 */
@Service
public class InvalidIdFailListener implements IInvokedMethodListener {

	private AnnotationInspector annotation;
	
	private RtNumberExtractor rtExtractor;
	
	@Inject
	public InvalidIdFailListener(RtNumberExtractor rtExtractor, AnnotationInspector annotation) {
		this.rtExtractor = rtExtractor;
		this.annotation = annotation;
	}
	
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		failInvalidId(method, testResult);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// Nothing to do here.
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		failInvalidId(method, testResult);
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		// Nothing to do here.
	}
	
	private void failInvalidId(IInvokedMethod method, ITestResult testResult) {
		if(	isTest(method) 
				&& rtExtractor.idsPresent(testResult) ) {
			
			Set<String> invalidIds = listInvalidIds(testResult);
			if( !invalidIds.isEmpty() ) {
				testResult.setStatus(TestStatus.FAILURE.getCode());
				testResult.setThrowable(new InvalidVersionOnIdException(invalidIds));
				TestReporter.logInfo(format("Test [%s] failed due to invalid Version One Ids: %s", method, invalidIds));
			}
		}
	}

	private Set<String> listInvalidIds(ITestResult result) {
		VersionOneService service = new ReadOnlyVersionOneServiceProvider();
		
		return rtExtractor.extractNumbers(result)
				.filter(id -> !service.getOriginalTestAssetId(id).isPresent())
				.collect(Collectors.toSet());
	}

	private boolean isTest(IInvokedMethod method) {
		return annotation.testFrom(method).isPresent();
	}
}
