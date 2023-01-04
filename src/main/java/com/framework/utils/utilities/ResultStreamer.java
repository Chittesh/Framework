package com.framework.utils.utilities;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Stream.concat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.TestResult;

import com.framework.utils.status.ResultStatusComparator;

public class ResultStreamer {
	
	private static final ResultStatusComparator METHOD_LEVEL_COMPARATOR = new ResultStatusComparator(ITestResult.FAILURE, ITestResult.SUCCESS, ITestResult.SKIP);
	private List<ITestResult> all;

	public ResultStreamer(ISuite suite) {
		all = mixAllTests(suite).collect(Collectors.toList());
	}
	
	public ResultStreamer(ITestContext context) {
		all = mix(context).collect(Collectors.toList());
	}
	
	
	/**
	 * Return all tests contained in the suite. 
	 * @return All tests.
	 */
	public Stream<ITestResult> getAllTests() {
		return all.stream();
	}
	
	public Stream<ITestResult> getTestsFilteredOfRetries() {
		return filterRetries(getAllTests());
	}

	private Stream<ITestResult> filterRetries(Stream<ITestResult> all) {
		return all.collect(
				groupingBy(
						ITestResult::getName, 
						collectingAndThen(maxBy(METHOD_LEVEL_COMPARATOR), 
								Optional::get) ))
				.values().stream();
	}

	private Stream<ITestResult> mixAllTests(ISuite suite) {
		return suite.getResults().values().stream()
			.map(result -> result.getTestContext())
			.flatMap(context -> mix(context));
	}
	
	private Stream<ITestResult> mix(ITestContext context) {
		Stream<ITestResult> failed = context.getFailedTests().getAllResults().stream();
		Stream<ITestResult> passed = context.getPassedTests().getAllResults().stream();
		Stream<ITestResult> skipped = context.getSkippedTests().getAllResults().stream();
		return concat(failed, concat(skipped, passed));
	}
}
