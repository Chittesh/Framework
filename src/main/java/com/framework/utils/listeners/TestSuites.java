package com.framework.utils.listeners;

import org.testng.*;
import org.testng.xml.XmlSuite;
import com.framework.utils.date.DateTimeConversion;
import com.framework.utils.date.SimpleDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.framework.utils.listeners.html.HtmlTags.*;

public class TestSuites implements IReporter {
	
	private static final String STRING_FORMATTER     = "%.3f sec";
	private static final String SUCCESS_STATUS       = "success";
	
	private List<ISuite> suites;
	
	public TestSuites(List<ISuite> suites) {
		this.suites = suites;
	}

	public String getProjectName(List<XmlSuite> xmlSuites) {
		Map<String, String> parameterMap = xmlSuites.get(0).getAllParameters();
		
		for (Map.Entry<String, String> entry : parameterMap.entrySet()) 
			if (entry.getKey().equalsIgnoreCase("projectName")) 
				return entry.getValue();
		
		return "";
	}

	public TestExecutionTotals testSuiteTotals() {
		int totalNumOfTests = 0;
		int totalNumOfPasses = 0;
		int totalNumOfFailures = 0;
		int totalNumOfSkips = 0;
		float totalRunTime = 0;

		for (ISuite suite : this.suites) {
			for (ISuiteResult r : suite.getResults().values()) {
				ITestContext overview = r.getTestContext();

				float totalTimeInMS   = (float)overview.getEndDate().getTime() - overview.getStartDate().getTime();

				totalNumOfTests    += overview.getAllTestMethods().length;
				totalNumOfPasses   += overview.getPassedTests().getAllResults().size();
				totalNumOfFailures += overview.getFailedTests().getAllResults().size();
				totalNumOfSkips    += overview.getSkippedTests().getAllResults().size();
				
				totalRunTime       += totalTimeInMS;
			}
		}
		
		TestExecutionTotals totals = new TestExecutionTotals();
		
		totals.setTotalNumOfTests(totalNumOfTests);
		totals.setTotalNumOfPasses(totalNumOfPasses);
		totals.setTotalNumOfFailures(totalNumOfFailures); 
		totals.setTotalNumOfSkips(totalNumOfSkips);
		totals.setTotalRunTime(totalRunTime);
		
		return totals;
	}
	
	public String dateTimeStamp() {
		return DateTimeConversion.convert(SimpleDate.getTimestamp(), "MM/dd/yyyy hh:mm aa");
	}
	
	public List<String> getSummaryRows() {
		List<String> summaryRows = new ArrayList<>();
		
		for (ISuite suite : this.suites) {
			for (ISuiteResult r : suite.getResults().values()) {
				ITestContext overview  = r.getTestContext();
				
				String testName = overview.getName();
				
				Set<ITestResult> failedTests  = overview.getFailedTests().getAllResults();
				Set<ITestResult> passedTests  = overview.getPassedTests().getAllResults();
				Set<ITestResult> skippedTests = overview.getSkippedTests().getAllResults();

				int numOfTests      = overview.getAllTestMethods().length;
				float totalTimeInMS = (float)overview.getEndDate().getTime() - overview.getStartDate().getTime();

				summaryRows.add(String.format(SUMMARY_ROW_TEMPLATE, testName, numOfTests, passedTests.size(),
				  			  			      skippedTests.size(), failedTests.size(), String.format(STRING_FORMATTER, totalTimeInMS / 1000)));

			}
		}
		
		return summaryRows;
	}
	
	public List<String> getDetailsRows() {
		List<String> detailRows = new ArrayList<>();
		
		detailRows.addAll(getFailedDetailRows());
		detailRows.addAll(getPassedDetailRows());
		detailRows.addAll(getSkippedDetailRows());
		
		return detailRows;
	}
	
	public String getSuiteTotal() {
		
		TestExecutionTotals totals = testSuiteTotals();
		
		return (String.format(SUMMARY_TOTAL_ROW_TEMPLATE, 
				  			  "TOTALS", 
							  totals.getTotalNumOfTests(), 
							  totals.getTotalNumOfPasses(),
							  totals.getTotalNumOfSkips(), 
							  totals.getTotalNumOfFailures(), 
							  String.format(STRING_FORMATTER, totals.getTotalRunTime() / 1000)));
		
	}

	public List<String> getFailedDetailRows() {
		List<String> failedRows = new ArrayList<>();
		
		for (ISuite suite : this.suites) 
			for (ISuiteResult r : suite.getResults().values()) {
				ITestContext testContext  = r.getTestContext();
				
				failedRows.addAll(
							detailRowsFor(
								testContext.getFailedTests().getAllResults(), testContext.getName()));
			}
		
		return failedRows;
	}	
	
	public List<String> getPassedDetailRows() {
		List<String> passedRows = new ArrayList<>();
		
		for (ISuite suite : this.suites) 
			for (ISuiteResult r : suite.getResults().values()) {
				ITestContext testContext  = r.getTestContext();
				
				passedRows.addAll(
							detailRowsFor(
								testContext.getPassedTests().getAllResults(), testContext.getName()));
			}
		
		return passedRows;
	}	

	public List<String> getSkippedDetailRows() {
		List<String> skippedRows = new ArrayList<>();
		
		for (ISuite suite : this.suites) 
			for (ISuiteResult r : suite.getResults().values()) {
				ITestContext testContext  = r.getTestContext();
				
				skippedRows.addAll(
								detailRowsFor(
									testContext.getSkippedTests().getAllResults(), testContext.getName()));
			}
		
		return skippedRows;
	}	
	
	public List<String> detailRowsFor(Set<ITestResult> tests, String suiteName) {
		List<String> detailsRows = new ArrayList<>();
		
		Iterator<ITestResult> iterator = tests.iterator();

		while (iterator.hasNext()) {
			TestResult element = new TestResult(iterator.next());
			
			detailsRows.add(String.format(DETAIL_ROW_TEMPLATE, 
										  element.getState(), 
										  suiteName, 
										  element.getTestClassName(), 
										  element.getTestMethodName(), 
										  element.getExecutionStatus(), 
										  element.getFailureReason(), 
										  element.getTotalDuration()));
		}
		
		return detailsRows;
	}
	
	public List<String> getSuiteAverageDetailsRows() {
		List<String> suiteAverageDetailRows = new ArrayList<>();
		
		for (ISuite suite : this.suites) 
			for (ISuiteResult r : suite.getResults().values()) {
				ITestContext testContext     = r.getTestContext();
				Set<ITestResult> passedTests = testContext.getPassedTests().getAllResults();

				suiteAverageDetailRows.addAll(getTestAverageDetailRows(passedTests, testContext.getName()));
			}
		
		Collections.sort(suiteAverageDetailRows);
		
		return suiteAverageDetailRows;
	}

	public List<String> getSuiteAverageExecutionDetailsRows() {
		List<String> suiteAverageExecutionDetailRows = new ArrayList<>();
		
		for (ISuite suite : this.suites) 
			for (ISuiteResult r : suite.getResults().values()) {
				ITestContext testContext     = r.getTestContext();
				Set<ITestResult> passedTests = testContext.getPassedTests().getAllResults();

				suiteAverageExecutionDetailRows.addAll(getTestAverageExecutionDetailRows(passedTests, testContext.getName()));
			}
		
		return suiteAverageExecutionDetailRows;
	}

	private List<String> getTestAverageExecutionDetailRows(Set<ITestResult> tests, String suiteName) {
		List<String> testAverageExecutionDetailRows = new ArrayList<>();
		
		Map<String, Integer> methodCountMap = getMethodCountMap(tests);
		Map<String, Float> methodAvgTimeMap = getMethodAverageTimeMap(tests);

		for (String methodName : methodCountMap.keySet()) {
			Integer methodCount     = methodCountMap.get(methodName);
			Float methodAverageTime = methodAvgTimeMap.get(methodName);
			
			String globalAverageTime = String.format(STRING_FORMATTER, methodAverageTime / methodCount);

			testAverageExecutionDetailRows.add(String.format(AVG_DETAIL_ROW_TEMPLATE, SUCCESS_STATUS, suiteName, methodName,
													  		 methodCount, String.format(STRING_FORMATTER, methodAverageTime), globalAverageTime));
		}
		
		return testAverageExecutionDetailRows;
	}

	private List<String> getTestAverageDetailRows(Set<ITestResult> tests, String suiteName) {
		List<String> testAverageDetailRows = new ArrayList<>();
		
		Map<String, Integer> methodCountMap = getMethodCountMap(tests);
		Map<String, Float> methodAvgTimeMap = getMethodAverageTimeMap(tests);

		Iterator<ITestResult> iterator = tests.iterator();
		while (iterator.hasNext()) {
			TestResult element = new TestResult(iterator.next());
			
			String methodName = element.getName();

			int methodCount     = methodCountMap.get(methodName);
			float methodAvgTime = methodAvgTimeMap.get(methodName);
			
			String avgMethodExecTime = String.format(STRING_FORMATTER, methodAvgTime/methodCount);

			testAverageDetailRows.add(String.format(DETAIL_ROW_TEMPLATE, SUCCESS_STATUS, suiteName, element.getTestClassName(),
												  methodName, methodCount, String.format(STRING_FORMATTER, element.getTotalTime()), avgMethodExecTime));
		}
		
		return testAverageDetailRows;
	}
	
	private Map<String, Float> getMethodAverageTimeMap(Set<ITestResult> tests) {
		Map<String, Float> methodAvgTimeMap = new LinkedHashMap<>();

		Iterator<ITestResult> iterator = tests.iterator();
		while (iterator.hasNext()) {
			TestResult element = new TestResult(iterator.next());
			
			String methodName = element.getName();
			float totalTime   = element.getTotalTime();

			float methodAvgTime = getFloatFromMap(methodAvgTimeMap, methodName) + totalTime;
			methodAvgTimeMap.put(methodName, methodAvgTime);
		}

		return methodAvgTimeMap;
	}

	private Map<String, Integer> getMethodCountMap(Set<ITestResult> tests) {
		Map<String, Integer> methodCountMap = new LinkedHashMap<>();

		Iterator<ITestResult> iterator = tests.iterator();
		while (iterator.hasNext()) {
			ITestResult element = iterator.next();
			
			String methodName = element.getName();

			int methodCount = getIntFromMap(methodCountMap, methodName) + 1;
			methodCountMap.put(methodName, methodCount);
		}

		return methodCountMap;
	}

	private int getIntFromMap(Map<String, Integer> map, String key) {
		return map.containsKey(key) ? map.get(key) : 0;
	}
	
	private float getFloatFromMap(Map<String, Float> map, String key) {
		return map.containsKey(key) ? map.get(key) : 0;
	}
	
}