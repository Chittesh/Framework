package com.framework.utils.utilities;

import java.util.stream.Stream;

import org.testng.ITestResult;

import com.framework.core.StreamUtils;
import com.framework.utils.Preamble;

public class PreambleRtNumberExtractor implements RtNumberExtractor {
	
	private AnnotationInspector annotation = new AnnotationInspector();
	
	@Override
	public boolean idsNotPresent(ITestResult testResult) {
		return extractNumbers(testResult)
				.findAny()
				.isEmpty();
	}

	@Override
	public Stream<String> extractNumbers(ITestResult testResult) {
		return annotation.preambleFrom(testResult)
				.map(Preamble::versionOneTestID)
				.filter(ids -> !"N/A".equals(ids[0]))
				.stream()
				.flatMap(StreamUtils::streamOf);
	}

	@Override
	public boolean idsPresent(ITestResult testResult) {
		
		return extractNumbers(testResult)
				.findAny()
				.isPresent();
	}

	@Override
	public String getMissingMessage() {
		return  "Method is missing Version One ID on @Preamble.";
	}
}
