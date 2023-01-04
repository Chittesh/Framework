package com.framework.utils.reporting;

public class ReporterConstants {
	
	public static final int NONE  = 0;
	public static final int INFO  = 1;
	public static final int DEBUG = 2;
	public static final int TRACE = 3;
	public static final int ERROR = 4;
	    
	public static final String ASSERT_TRUE           = "Assert True";
	public static final String ASSERT_FAIL           = "Assert Fail";
	public static final String ASSERT_FALSE          = "Assert False";
	public static final String ASSERT_EQUALS         = "Assert Equals";
	public static final String ASSERT_NOT_EQUALS     = "Assert Not Equals";
	public static final String ASSERT_GREATER_THAN_0 = "Assert Greater Than Zero";
	public static final String ASSERT_NULL           = "Assert Null";
	public static final String ASSERT_NOT_NULL       = "Assert Not Null";
	public static final String SOFT_ASSERT_TRUE      = "Soft Assert True";
	public static final String SOFT_ASSERT_FALSE     = "Soft Assert False";
	public static final String SOFT_ASSERT_EQUALS    = "Soft Assert Equals";
	public static final String SOFT_ASSERT_NULL      = "Soft Assert Null";
	public static final String SOFT_ASSERT_NOT_NULL  = "Soft Assert Not Null";
	    
	public static final String GREEN_ROW   = "%s <font size=2 color='green'><b> Pass - %s - %s</b></font><br />";
	public static final String RED_ROW     = "%s <font size=2 color='red'><b> Fail - %s - %s</b></font><br />";                                            

	public static final String PASSING_ROW = "%s Pass - %s - %s";
	public static final String FAILING_ROW = "%s Fail - %s - %s";
	
	private ReporterConstants() {
	
	}
	
}        
