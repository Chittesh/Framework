package com.framework.utils.listeners.html;

public class HtmlTags {
	
	private HtmlTags() {
		
	}
		
	public static final String JUMBOTRON_CLASS 					= ".jumbotron {";
	
	public static final String DIV_CLASS		 				= "<div class=\"%s\">";
	public static final String DIV_END 							= "</div>";
	public static final String JUMBOTRON_DIV 					= String.format(DIV_CLASS, "jumbotron text-center");
	public static final String CONTAINER_DIV 					= String.format(DIV_CLASS, "container");
	
	public static final String TBODY_ID             			= "<tbody id=\"%s\">";
	public static final String TBODY_END 						= "</tbody>";
	public static final String TBODY_VALUE_END                  = "%s</tbody>";
	
	public static final String SUITE_AVG_EXECUTION_DETAIL_TBODY = String.format(TBODY_ID, "suiteAvgExecutionDetail");
	public static final String SUITE_AVG_DETAIL_TBODY 	        = String.format(TBODY_ID, "suiteAvgDetailTable");
	public static final String SUITE_DETAIL_TBODY 			    = String.format(TBODY_ID, "suiteDetailTable");
	public static final String ID_SUITE_SUMMARY_TBODY 			= String.format(TBODY_ID, "suiteSummaryTable");
	

	public static final String TR 								= "<tr>";
	public static final String TR_END 							= "</tr>";
	
	public static final String THEAD 							= "<thead>";
	public static final String THEAD_END 						= "</thead>";
	
	public static final String TABLE 							= "<table class=\"table\">";
	public static final String TABLE_END 						= "</table>";
	
	public static final String BODY 							= "<body>";
	public static final String BODY_END 						= "</body>";
	
	public static final String HEAD 						    = "<head>";
	public static final String HEAD_END 						= "</head>";
	
	public static final String STYLE_END 						= "</style>";
	public static final String STYLE 							= "<style>";
	
	public static final String TH								= "<th>%s</th>";
	public static final String TOTAL_EXECUTION_TIME_TH 			= String.format(TH, "Total Execution Time");
	public static final String TEST_CLASS_TH 					= String.format(TH, "Test Class");
	public static final String TEST_METHOD_TH 					= String.format(TH, "Test Method");
	public static final String TEST_GROUP_NAME_TH 				= String.format(TH, "Test Group Name");
	public static final String FAILURE_REASON_TH				= String.format(TH, "Failure Reason");
	public static final String STATUS_TH 						= String.format(TH, "Status");
	public static final String AVERAGE_EXECUTION_TIME 			= String.format(TH, "Average Execution Time");
	public static final String TEST_METHOD_COUNT_TH				= String.format(TH, "Test Method Count");
	public static final String EXECUTION_TIME_TH				= String.format(TH,  "Execution Time");
	public static final String FAILED_COUNT_TH					= String.format(TH, "# Failed");
	public static final String SKIPPED_COUNT_TH					= String.format(TH, "# Skipped");
	public static final String PASSED_COUNT_TH 					= String.format(TH, "# Passed");
	public static final String NUMBER_OF_TESTS_TH				= String.format(TH, "# of Tests");
	
	public static final String H1 								= "<h1>%s</h1>";
	
	public static final String H2 								= "<h2>%s</h2>";
	public static final String H2_END                       	= "</h2>";
	
	public static final String H2_ID_PROJECT_TEAM           	= "<h2 id=\"projectTeam\">";
	public static final String PROJECT_TEAM_H2 					= "<h2 id=\"projectTeam\"></h2>";
	public static final String TEST_EXECUTION_SUMMARY_H2 		= String.format(H2, "Test Execution Summary");
	
	public static final String DATE_TIME_STAMP_H3				= "<h3 id=\"dateTimeStamp\"></h3>";
	
	public static final String DOCTYPE 							= "<!DOCTYPE html>";
	
	public static final String HTML_END 						= "</html>";
	public static final String HTML 							= "<html>";
	
	public static final String BLACK 							= "black";
	
	public static final String NEW_LINE 						= "\r\n";
	
	public static final String TITLE 							= "<title>%s</title>";
	public static final String TH_STYLED						= "<th bgcolor=\"%s\"><font color=\"%s\" size=\"%d\">%s</font></th>";

	
	public static final String STYLE_INFO = STYLE + NEW_LINE + 
												JUMBOTRON_CLASS + NEW_LINE + 
												"background-color: %s;" + NEW_LINE + 
												"color: %s;" + NEW_LINE + 
												"}" + NEW_LINE + 
												STYLE_END;

	public static final String META_KEYWORDS = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" + NEW_LINE + 
			"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">" + NEW_LINE + 
			"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>" + NEW_LINE + 
			"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>";
	
	
	public static final String SUMMARY_ROW_TEMPLATE       = "<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";
	public static final String DETAIL_ROW_TEMPLATE        = "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";
	public static final String SUMMARY_TOTAL_ROW_TEMPLATE = "<tr class= \"lead\" style=\"background-color:LightGrey\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";
	public static final String AVG_DETAIL_ROW_TEMPLATE    = "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";

	
	
	
}
