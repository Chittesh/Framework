package com.framework.utils;

import java.util.Arrays;

public class BrowserTypes {
	
	private BrowserTypes() {
		
	}
	
	public static final String CHROMEMOBILEEMULATOR = "chromemobileemulator";
	public static final String CHROME = "chrome";
	public static final String FIREFOX = "firefox";

	public static boolean isValid(String browser) {
		return Arrays.asList(FIREFOX, CHROMEMOBILEEMULATOR, CHROME).contains(browser); 
	}
	
}
