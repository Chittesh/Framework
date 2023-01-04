package com.framework.utils;

public class Sleeper {

	private Sleeper() {
		
	}
   
    public static void sleep(double d) {
    	try {
    		Thread.sleep((long) (d * 1000));
    	}
    	catch (Exception e) {
    		//nothing
    	}
    }
}