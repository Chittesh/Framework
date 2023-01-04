package com.framework.utils.listeners.html;

import static com.framework.utils.listeners.html.HtmlTags.*;

public class ReportTemplate {

	private ReportTemplate() {
		
	}
	
	public static String getAnalysisReportTemplate() {
		return  addOnNewLine(DOCTYPE) 													   + 
				addOnNewLine(HTML)    													   +
				
					addOnNewLine(HEAD) 													   + 
						addOnNewLine(String.format(TITLE, "Allegis Emailable Report"))     + 
						addOnNewLine(META_KEYWORDS)						 	 		   	   +
						addOnNewLine(String.format(STYLE_INFO, "#DB9CFB", "black"))    	   +
					addOnNewLine(HEAD_END) 											       +
				    
					addOnNewLine(BODY) 													   +
						addOnNewLine(JUMBOTRON_DIV) 									   + 
							addOnNewLine(String.format(H1, "Allegis Automation Analysis")) + 
							addOnNewLine(PROJECT_TEAM_H2) 								   +  
							addOnNewLine(DATE_TIME_STAMP_H3) 							   +  
						addOnNewLine(DIV_END) 							       			   +  
				    	
						addOnNewLine(CONTAINER_DIV)										   + 
							addOnNewLine(TEST_EXECUTION_SUMMARY_H2) 					   + 
							addOnNewLine(testExecutionStatusAnalysisTable()) 			   + 
						addOnNewLine(DIV_END) 											   +
				    	
					addOnNewLine(BODY_END) 												   +
					
				addOnNewLine(HTML_END);
	}
	
	public static String getPerformanceReportTemplate() {
		return  addOnNewLine(DOCTYPE) +
				addOnNewLine(HTML) +
				
					addOnNewLine(HEAD) + 
						addOnNewLine(String.format(TITLE, "Allegis Performance Test Emailable Report")) +
						addOnNewLine(META_KEYWORDS)						  								+
						addOnNewLine(String.format(STYLE_INFO,  "#c9740a", "#fff"))					    +
					addOnNewLine(HEAD_END)															    + 
				
				addOnNewLine(BODY)																	    +
					addOnNewLine(JUMBOTRON_DIV)														    + 
						addOnNewLine(String.format(H1, "Performance Test Automation Report"))			+ 
						addOnNewLine(PROJECT_TEAM_H2)												    + 
						addOnNewLine(DATE_TIME_STAMP_H3) 												+ 
					addOnNewLine(DIV_END + NEW_LINE)												    +
				
					addOnNewLine(CONTAINER_DIV)														    + 
						addOnNewLine(TEST_EXECUTION_SUMMARY_H2)											+
						addOnNewLine(testExecutionStatusDetailsTable())									+
					addOnNewLine(DIV_END)																+
				
					addOnNewLine(CONTAINER_DIV)															+ 
						addOnNewLine(String.format(H2, "Method Execution Average Time Details"))		+ 
						addOnNewLine(methodExecutionAverageTimeDetailsTable())							+
					addOnNewLine(DIV_END)																+ 
					
					addOnNewLine(CONTAINER_DIV)															+
						addOnNewLine(String.format(H2,  "Test Execution Average Time Details"))			+ 
						addOnNewLine(testExecutionAverageTimeDetailsTable())							+
					addOnNewLine(DIV_END)																+ 
					
					addOnNewLine(CONTAINER_DIV)															+
						addOnNewLine(String.format(H2, "Test Execution Details"))						+ 
						addOnNewLine(testExecutionDetailsTable())										+ 
					addOnNewLine(DIV_END)																+ 
				addOnNewLine(BODY_END)																	+
				
			addOnNewLine(HTML_END);
	}
	
	public static String getCustomizedReportTemplate() {
		return addOnNewLine(DOCTYPE) +
			   addOnNewLine(HTML) +
			   
			   		addOnNewLine(HEAD) + 
			   			addOnNewLine(String.format(TITLE, "Allegis Emailable Report"))					+ 
			   			addOnNewLine(META_KEYWORDS)														+
			   			addOnNewLine(String.format(STYLE_INFO, "#03274A", "#fff"))						+
			   		addOnNewLine(HEAD_END)																+
				
			   		addOnNewLine(BODY)																	+
				
			   			addOnNewLine(JUMBOTRON_DIV)														+ 
			   				addOnNewLine(String.format(H1, "Allegis Automation Report"))				+ 
			   				addOnNewLine(PROJECT_TEAM_H2)												+ 
			   				addOnNewLine(DATE_TIME_STAMP_H3)											+ 
			   			addOnNewLine(DIV_END)															+ 
				
			   			addOnNewLine(CONTAINER_DIV)														+ 
			   				addOnNewLine(TEST_EXECUTION_SUMMARY_H2)										+ 
			   				addOnNewLine(testExecutionStatusDetailsTable())								+
			   			addOnNewLine(DIV_END)															+ 
						
			   			addOnNewLine(CONTAINER_DIV)														+ 
			   				addOnNewLine(String.format(H2,  "Test Execution Details"))					+ 
			   				addOnNewLine(testExecutionDetailsTable())									+ 
			   			addOnNewLine(DIV_END)															+
			   			
			   		addOnNewLine(BODY_END) 																+
			   		
		HTML_END;
	}
	
	private static String testExecutionDetailsTable() {
		return addOnNewLine(TABLE) 								+ 
					
					addOnNewLine(THEAD) 						+ 
						addOnNewLine(TR) 						+ 
							addOnNewLine(TEST_GROUP_NAME_TH)  	+ 
							addOnNewLine(TEST_CLASS_TH)  		+ 
							addOnNewLine(TEST_METHOD_TH)  		+ 
							addOnNewLine(STATUS_TH)  			+ 
							addOnNewLine(FAILURE_REASON_TH)  	+	 
							addOnNewLine(EXECUTION_TIME_TH)  	+ 
						addOnNewLine(TR_END)  					+
					
					addOnNewLine(THEAD_END)					 	+	 

					addOnNewLine(SUITE_DETAIL_TBODY) 			+ 
					addOnNewLine(TBODY_END) 					+
		
			addOnNewLine(TABLE_END);
	}
	
	private static String testExecutionAverageTimeDetailsTable() {  
		return addOnNewLine(TABLE) 									+

				addOnNewLine(THEAD) 								+
				
					addOnNewLine(TR) 								+ 
							addOnNewLine(TEST_GROUP_NAME_TH) 		+ 
							addOnNewLine(TEST_CLASS_TH) 			+ 
							addOnNewLine(TEST_METHOD_TH) 			+ 
							addOnNewLine(TEST_METHOD_COUNT_TH) 		+ 
							addOnNewLine(EXECUTION_TIME_TH) 		+ 
							addOnNewLine(AVERAGE_EXECUTION_TIME) 	+ 
					addOnNewLine(TR_END) 							+ 
					
				addOnNewLine(THEAD_END) 							+ 

				addOnNewLine(SUITE_AVG_DETAIL_TBODY) 				+ 
				addOnNewLine(TBODY_END) 							+
					
		addOnNewLine(TABLE_END);	
	}
	
	private static String methodExecutionAverageTimeDetailsTable() {
	
		return addOnNewLine(TABLE) 									+
				
					addOnNewLine(THEAD) 							+ 
				
						addOnNewLine(TR) 							+ 
							addOnNewLine(TEST_GROUP_NAME_TH) 		+ 
							addOnNewLine(TEST_METHOD_TH) 			+ 
							addOnNewLine(TEST_METHOD_COUNT_TH) 		+ 
							addOnNewLine(TOTAL_EXECUTION_TIME_TH) 	+ 
							addOnNewLine(AVERAGE_EXECUTION_TIME) 	+ 
						addOnNewLine(TR_END) 						+ 
					
					addOnNewLine(THEAD_END) 						+ 

					addOnNewLine(SUITE_AVG_EXECUTION_DETAIL_TBODY) 	+ 
					addOnNewLine(TBODY_END) 						+
				
			  addOnNewLine(TABLE_END);
		
	}
	
	private static String testExecutionStatusDetailsTable() {
		
		return 
			addOnNewLine(TABLE) +
				
				addOnNewLine(THEAD) +
				
					addOnNewLine(TR) + 
						addOnNewLine(TEST_GROUP_NAME_TH) + 
						addOnNewLine(NUMBER_OF_TESTS_TH) + 
						addOnNewLine(PASSED_COUNT_TH) + 
						addOnNewLine(SKIPPED_COUNT_TH) + 
						addOnNewLine(FAILED_COUNT_TH) + 
						addOnNewLine(EXECUTION_TIME_TH) + 
					addOnNewLine(TR_END) +
					
				addOnNewLine(THEAD_END) +
			
				addOnNewLine(ID_SUITE_SUMMARY_TBODY) + 
				addOnNewLine(TBODY_END) +
				
			addOnNewLine(TABLE_END);
		
	}
	
	private static String testExecutionStatusAnalysisTable() {
		
		return addOnNewLine(TABLE) +
				
					addOnNewLine(THEAD) +
					
						addOnNewLine(TR) + 
							addOnNewLine(String.format(TH_STYLED, "#AFF4FA", BLACK, 4, "Test Group Name")) + 
							addOnNewLine(String.format(TH_STYLED, "#F9BC81", BLACK, 4, "# of Tests")) + 
							addOnNewLine(String.format(TH_STYLED, "#BEFBAC", BLACK, 4, "# Passed")) + 
							addOnNewLine(String.format(TH_STYLED, "#F7F380", BLACK, 4, "# Skipped")) + 
							addOnNewLine(String.format(TH_STYLED, "#FCA0A0", BLACK, 4, "# Failed")) + 
							addOnNewLine(String.format(TH_STYLED, "#BFBEFC", BLACK, 4, "Execution Time")) + 
						addOnNewLine(TR_END) + 
			
					addOnNewLine(THEAD_END) + 
			
					addOnNewLine(ID_SUITE_SUMMARY_TBODY) + 
					addOnNewLine(TBODY_END) + 
					
			  addOnNewLine(TABLE_END);
		
	}
	
	private static String addOnNewLine(String text) {
		return text + NEW_LINE;
	}
	
}