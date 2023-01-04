package com.framework.utils.listeners;

import org.testng.*;
import org.testng.xml.XmlSuite;
import com.framework.utils.AuditLogger;
import java.util.List;
import java.util.stream.Collectors;

public class CustomizedEmailableReport implements IReporter {

	private static final String TEMPLATE_PATH = "src/main/resources/html/reportTemplate.html";
	private static final String REPORT_NAME   = "emailable-automation-report-NEW.html";

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		
		AuditLogger.reset();
		AuditLogger.collect("( CustomizedEmailableReport - generateReport ), ");
		AuditLogger.log(0);
		
		String projectName = EmailableReport.getProjectName(xmlSuites);
		
		EmailableReport report = new EmailableReport(REPORT_NAME);
		report.initializeTemplate(TEMPLATE_PATH);
		
		TestSuites testSuites = new TestSuites(suites);

		String suiteTotal        = testSuites.getSuiteTotal();
		List<String> detailRows  = testSuites.getDetailsRows();
		List<String> summaryRows = testSuites.getSummaryRows();
		summaryRows.add(suiteTotal);
		
		String summaryInfo = toString(summaryRows);
		String detailInfo  = toString(detailRows);
		
		report.updateTemplateWithSummaryInfo(summaryInfo)
			  .updateTemplateWithDetailInfo(detailInfo)
			  .updateTemplateWithProjectName(xmlSuites, projectName);

		report.updateReport(outputDirectory);
		
	}	
	
	private String toString(List<String> list) {
		return list.stream().collect(Collectors.joining());
	}
	
}