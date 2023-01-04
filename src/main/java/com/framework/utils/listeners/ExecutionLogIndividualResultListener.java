package com.framework.utils.listeners;

import static com.framework.utils.status.TestStatus.from;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.testng.ITestResult;

import com.allegis.service.phoenix.ExecutionLog;
import com.allegis.service.phoenix.impl.NewrelicExecutionLog;
import com.framework.core.StreamUtils;
import com.framework.utils.ErrorInfo;
import com.framework.utils.ExceptionErrorMapping;
import com.framework.utils.TestEnvironmentExecutionContext;
import com.framework.utils.TestInfo;
import com.framework.utils.status.TestStatus;
import com.framework.utils.utilities.AnnotationInspector;
import com.framework.utils.utilities.RtNumberExtractor;


@Service
@Profile("!drylog")
public class ExecutionLogIndividualResultListener extends IndividualResultReportListener implements ExecutionLogListener {
	
	private ObjectProvider<ExecutionLog> executionLog;
	
	private Map<TestStatus, String> commentPrefix = new HashMap<>();

	private NewrelicExecutionLog newrelicLog;

	@Inject
    public ExecutionLogIndividualResultListener(Configuration config, AnnotationInspector annotation, ObjectProvider<ExecutionLog> executionLog, RtNumberExtractor rtExtractor, NewrelicExecutionLog newrelicLog) {
		super(config, annotation, rtExtractor);
		
		this.executionLog = executionLog;
		this.newrelicLog = newrelicLog;
		
		commentPrefix.put(TestStatus.SUCCESS, "PASS");
		commentPrefix.put(TestStatus.FAILURE, "FAIL");
		commentPrefix.put(TestStatus.SKIP, "SKIP");
	}
	
	protected void report(String message, ITestResult testResult, String ... ids) {
		ExecutionLog logger = executionLog.getIfAvailable(DummyLogger::new);
		
		Integer status = from(testResult).getVOneCode();
		
		StreamUtils.streamOf(ids)
			.forEach(id -> log(message, testResult, logger, status, id));
	}

	private void log(String message, ITestResult testResult, ExecutionLog logger, Integer status, String id)  {
		
		TestInfo testInfo  = new TestInfo(testResult);
		
		Date startTime     = testInfo.getStartDate();
		Date endTime       = testInfo.getEndDate();
		String method      = testInfo.getMethodQualifiedName();
		long executionTime = testInfo.getExecutionTime();
		String testGroups  = testInfo.getTestGroups();
		String exception   = testInfo.getException();
		
		String prefixComment = prefixComment(message,from(testResult));
		ErrorInfo error      = getError(exception);
		
		TestEnvironmentExecutionContext tec = (TestEnvironmentExecutionContext) testResult.getTestContext()
																						  .getAttribute("testEnvironmentContext");
		
		Map<String, String> contextMap = new HashMap<>();
		
		contextMap.put("applicationUnderTest", tec.getApplicationUnderTest());
		contextMap.put("browserUnderTest"    , tec.getBrowserUnderTest());
		contextMap.put("browserVersion"      , tec.getBrowserVersion());
		contextMap.put("browserSize"         , tec.getBrowserSize());
		contextMap.put("operatingSystem"     , tec.getOperatingSystem());
		contextMap.put("runLocation"         , tec.getRunLocation());

		contextMap.put("description"         , tec.getDescription());
		contextMap.put("environment"         , tec.getEnvironment());
		contextMap.put("testName"            , tec.getTestName());
		contextMap.put("pageUrl"             , tec.getPageUrl());
		contextMap.put("userRole"            , tec.getUserRole());
		contextMap.put("frameworkVersion"    , tec.getFrameworkVersion());
		contextMap.put("gridUrl"             , tec.getGridUrl());
		contextMap.put("testClass"           , tec.getTestClass().getCanonicalName());
		
		logger.log(startTime, 
				   endTime, 
				   status, 
				   id, 
				   method, 
				   prefixComment, 
				   executionTime, 
				   testGroups, 				 
				   contextMap, 
				   error);
		
		newrelicLog.log(startTime, 
						endTime, 
						status, 
						id, 
						method, 
						prefixComment, 
						executionTime, 
						testGroups, 						
						tec, 
						error);
		
	}
	
	private ErrorInfo getError(String exception) {
		
		for (ErrorInfo errorInfo : ExceptionErrorMapping.getMapping()) 
		    if (exception.toLowerCase().contains(errorInfo.getException().toLowerCase())) 
		        return errorInfo;

		return ExceptionErrorMapping.emptyError();
		
	}
	
	private static class DummyLogger implements ExecutionLog {

		@Override
		public void log(Date start, 
						Date end, 
						Integer statusId, 
						String testId, 
						String method, 
						String comment, 
						long executionTime, 
						String testGroups, 						
						Map<String, String> context, 
						Object error) 
		{
			// Nothing to do
		}
		
	}
	
	protected String prefixComment(String comment, TestStatus status) {
		return String.format("%s--selenium:+%s",  mapPrefix(status), comment);
	}
	
	public String mapPrefix(TestStatus status) {
		return commentPrefix.containsKey(status) ? commentPrefix.get(status) : "";
	}
	
}
