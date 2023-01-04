package com.framework.utils.listeners;

import org.testng.*;
import org.testng.xml.XmlSuite;
import com.framework.utils.AuditLogger;
import com.framework.utils.listeners.html.*;

import java.util.List;
import java.util.stream.Collectors;

public class AnalysisEmailableReport implements IReporter {

	private static final String REPORT_NAME = "analysis-automation-report-NEW.html";
	
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

		AuditLogger.reset();
		AuditLogger.collect("( AnalysisEmailableReport - generateReport ), ");
		AuditLogger.log(0);
		
		String projectName = EmailableReport.getProjectName(xmlSuites);
		
		String template = ReportTemplate.getAnalysisReportTemplate();
		
		EmailableReport report = new EmailableReport(template, REPORT_NAME);
		
		TestSuites testSuites = new TestSuites(suites);

		String summaryInfo = toString(testSuites.getSummaryRows());
		String detailInfo  = toString(testSuites.getDetailsRows());
		
		report.updateTemplateWithSummaryInfo(summaryInfo)
			  .updateTemplateWithDetailInfo(detailInfo)
			  .updateTemplateWithProjectName(xmlSuites, projectName);

		report.updateReport(outputDirectory);
		
	}
	
	private String toString(List<String> list) {
		return list.stream().collect(Collectors.joining());
	}

}