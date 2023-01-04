package com.framework.utils.listeners;

import org.testng.*;
import org.testng.xml.XmlSuite;
import com.framework.exception.AutomationException;
import com.framework.utils.date.DateTimeConversion;
import com.framework.utils.date.SimpleDate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.framework.utils.listeners.html.HtmlTags.*;

public class EmailableReport implements IReporter {
	
	private String template;
	private String reportName;
	
	public EmailableReport(String template, String reportName) {
		this.template   = template;
		this.reportName = reportName;
	}
	
	public EmailableReport(String reportName) {
		this.reportName = reportName;
	}

	public static String getProjectName(List<XmlSuite> xmlSuites) {
		Map<String, String> parameterMap = xmlSuites.get(0).getAllParameters();
		
		for (Map.Entry<String, String> entry : parameterMap.entrySet()) 
			if (entry.getKey().equalsIgnoreCase("projectName")) 
				return entry.getValue();
		
		return "";
	}

	public void updateReport(String outputDirectory) {
		new File(outputDirectory).mkdirs();

		try (PrintWriter reportWriter = new PrintWriter(
											new BufferedWriter(
												new FileWriter(new File(outputDirectory, this.reportName))), true)) 
		{
			reportWriter.println(this.template);
		} catch (IOException e) {
			throw new AutomationException("Could not write to report template.  Error: " + e.getMessage());
		}
	}
	
	public static String dateTimeStamp() {
		return DateTimeConversion.convert(SimpleDate.getTimestamp(), "MM/dd/yyyy hh:mm aa");
	}
	
	public void initializeTemplate(String reportTemplatePath) {
		try {
			byte[] reportTemplate = Files.readAllBytes(Paths.get(reportTemplatePath));
			
			this.template = new String(reportTemplate, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new AutomationException("Could not find the report template file to convert to string.  Error: " + e.getMessage());
		}
	}
	
	public EmailableReport updateTemplateWithSummaryInfo(String summaryRowsInfo) {
		checkTemplateIsNotEmpty();
		
		this.template = this.template.replaceFirst(ID_SUITE_SUMMARY_TBODY, String.format(TBODY_VALUE_END, summaryRowsInfo));
		
		return this;
	}
	
	
	public EmailableReport updateTemplateWithDetailInfo(String detailRowsInfo) {
		checkTemplateIsNotEmpty();
		
		this.template = this.template.replaceFirst(SUITE_DETAIL_TBODY, String.format(TBODY_VALUE_END, detailRowsInfo))
	      	 	    		.replaceFirst(DATE_TIME_STAMP_H3, DATE_TIME_STAMP_H3 + EmailableReport.dateTimeStamp());
		
		return this;
	}
	
	public EmailableReport updateTemplateWithProjectName(List<XmlSuite> xmlSuites, String projectName) {
		checkTemplateIsNotEmpty();
		
		if (xmlSuites.get(0).getAllParameters().containsKey("projectName"))
			this.template = this.template.replaceFirst(H2_ID_PROJECT_TEAM + H2_END, H2_ID_PROJECT_TEAM + projectName + H2_END);
		
		return this;
	}
	
	public EmailableReport updateTemplateWithAverageExecutionInfo(String averageDetailExecutionRowsInfo) {
		checkTemplateIsNotEmpty();
		
		this.template =  this.template.replaceFirst(SUITE_AVG_EXECUTION_DETAIL_TBODY, String.format(TBODY_VALUE_END, averageDetailExecutionRowsInfo));
		
		return this;
	}
	
	public EmailableReport updateTemplateWithAverageDetailInfo(String averageDetailRowsInfo) {
		checkTemplateIsNotEmpty();
		
		this.template = this.template.replaceFirst(SUITE_AVG_DETAIL_TBODY, String.format(TBODY_VALUE_END, averageDetailRowsInfo));
		
		return this;
	}
	
	private void checkTemplateIsNotEmpty() {
		if (this.template.isBlank())
			throw new AutomationException("template is empty!");
	}
	
}