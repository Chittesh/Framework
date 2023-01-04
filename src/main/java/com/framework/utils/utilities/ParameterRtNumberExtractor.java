package com.framework.utils.utilities;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.testng.ITestResult;

import com.framework.core.StreamUtils;

public class ParameterRtNumberExtractor implements RtNumberExtractor {
	
	private static final String RT_NUMBER_LIST_PATTERN = "\\bRT-\\d+\\b(?:\\s*,\\s*\\bRT-\\d+\\b)*";
	
	@Override
	public Stream<String> extractNumbers(ITestResult testResult) {

		return parametersStream(testResult)
				.filter(param -> param.matches(RT_NUMBER_LIST_PATTERN))
				.flatMap(param -> Stream.of(param.split("\\s*,\\s*")));
	}

	@Override
	public boolean idsNotPresent(ITestResult testResult) {
	
		return  parametersStream(testResult)
				.noneMatch(param -> param.matches(RT_NUMBER_LIST_PATTERN));
		
	}
	
	@Override
	public boolean idsPresent(ITestResult testResult) {
		
		return parametersStream(testResult)
				.anyMatch(param -> param.matches(RT_NUMBER_LIST_PATTERN));
		
	}

	private Stream<String> parametersStream(ITestResult testResult) {
		Object[] parameters = Method.class.isInstance(testResult.getParameters()[0])
				? (Object[]) testResult.getParameters()[1]
				: testResult.getParameters();
	
		return StreamUtils.streamOf(parameters).map(Object::toString);
	}

	@Override
	public String getMissingMessage() {
		return  "Method is missing Version One ID on parameters.";
	}
}
