package com.framework.utils.listeners;

import org.testng.*;
import org.testng.xml.XmlSuite;
import com.framework.utils.AuditLogger;
import com.framework.utils.listeners.html.ReportTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class PerformanceEmailableReport implements IReporter {

	private static final String REPORT_NAME = "performance-automation-report-NEW.html";
	
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

		AuditLogger.reset();
		AuditLogger.collect("( PerformanceEmailableReport - generateReport ), ");
		AuditLogger.log(0);
		
		String projectName = EmailableReport.getProjectName(xmlSuites);
		
		String template = ReportTemplate.getPerformanceReportTemplate();

		EmailableReport report = new EmailableReport(template, REPORT_NAME);
		
		TestSuites testSuites = new TestSuites(suites);
		
		String suiteTotal                       = testSuites.getSuiteTotal();
		List<String> detailRows                 = testSuites.getDetailsRows();
		List<String> averageDetailRows          = testSuites.getSuiteAverageDetailsRows();
		List<String> averageExecutionDetailRows = testSuites.getSuiteAverageExecutionDetailsRows();
		List<String> summaryRows                = testSuites.getSummaryRows();
		summaryRows.add(suiteTotal);
		
		String summaryRowsInfo                = toString(summaryRows);
		String detailRowsInfo                 = toString(detailRows);
		String averageDetailRowsInfo          = toString(averageDetailRows);
		String averageDetailExecutionRowsInfo = toString(averageExecutionDetailRows);
		
		report.updateTemplateWithSummaryInfo(summaryRowsInfo)
			  .updateTemplateWithAverageExecutionInfo(averageDetailExecutionRowsInfo)
			  .updateTemplateWithAverageDetailInfo(averageDetailRowsInfo)
			  .updateTemplateWithDetailInfo(detailRowsInfo)
			  .updateTemplateWithProjectName(xmlSuites, projectName);

		report.updateReport(outputDirectory);
		
	}
	
	public static String toString(List<String> list) {
		return list.stream().collect(Collectors.joining());
	}

}