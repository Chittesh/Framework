package com.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.framework.exception.AutomationException;

public class PropertyInfo {

	private String propertyFile;

	public PropertyInfo(String propertyFile) {
		this.propertyFile = propertyFile;
	}
	
	public String getSonarSkip() {
    	return getProperty("sonar.skip");
    }
	
	private String getProperty(String name) {
		Properties properties = loadProperties();
		String property       = properties.getProperty(name);
		
		return property != null ? property : "";
	}
	
	private Properties loadProperties() {
		
		try (FileInputStream inputStream = new FileInputStream(propertyFile)){
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		} catch (IOException e) {
			throw new AutomationException("cannot read from " + this.propertyFile);
		}
		
	}

}
