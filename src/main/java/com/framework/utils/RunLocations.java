package com.framework.utils;

import java.util.Arrays;
import java.util.List;

public class RunLocations {
 		
	public static final String API = "api";
 	public static final String GRID = "grid";
 	public static final String MOBILE = "mobile";
 	public static final String LOCAL = "local";
 	public static final String DOCKER = "docker";
 	public static final String SIMULATOR = "simulator";
 	public static final String BROWSERSTACK = "browserstack";
 		
 	public static final String JENKINS_PARAMETER = "jenkinsParameter";
 	public static final String JENKINS_BROWSER_VERSION = "jenkinsBrowserVersion";
 
 	private RunLocations() {
 		
 	}
 	
 	public static boolean isValid(String runLocation) {
    	List<String> allowedRunLocations = Arrays.asList(LOCAL, 
				 									     BROWSERSTACK, 
				 									     GRID, 
				 									     DOCKER,
				 									     SIMULATOR,
				 									     MOBILE,
				 									     API);

    	return allowedRunLocations.contains(runLocation.toLowerCase());
    }

 }