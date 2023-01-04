package com.framework.utils.utilities;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.combined.CombinedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationBuilder {

	private Logger logger = LogManager.getLogger(this.getClass());
	
	private Configuration config;

	public ConfigurationBuilder() {
		Parameters params = new Parameters();
		
		CombinedConfigurationBuilder builder = new CombinedConfigurationBuilder()
				.configure(params.fileBased().setFile(new File("master-config.xml")));
				
		try {
			config = builder.getConfiguration();
		
		} catch (ConfigurationException e) {
			logger.error("Failed to load configuration for the framework.", e);
			throw new RuntimeException("Failed to load configuration.", e);
		}
	}

	public Configuration getConfiguration() {
		return config;
	}
    
}
