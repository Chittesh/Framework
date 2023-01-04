package com.framework.config;

import java.io.File;

import org.apache.commons.configuration2.builder.combined.CombinedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import com.framework.utils.utilities.ConfigurationBuilder;

@Configuration
@PropertySource(value = "classpath:automation-framework-defaults.properties")
@PropertySource(value = "classpath:automation-framework.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:Config.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:Credentials.properties", ignoreResourceNotFound = false)
@PropertySource(value = "classpath:EnvironmentURLs.properties", ignoreResourceNotFound = false)
@ComponentScan(basePackages = "com.framework")
public class PropertiesConfig {

	private Logger logger = LogManager.getLogger();
	
	@Bean
	public ConfigurationBuilder configurationBuilder() {
		return new ConfigurationBuilder();
	}
	
	@Bean(name = "config")
	@Primary
	public org.apache.commons.configuration2.Configuration configuration(ConfigurationBuilder builder) {
		return builder.getConfiguration();
	}
	
	@Bean(name = "urlConfig")
	public org.apache.commons.configuration2.Configuration urlConfiguration() throws ConfigurationException {
			Parameters params = new Parameters();
		
		CombinedConfigurationBuilder builder = new CombinedConfigurationBuilder()
				.configure(params.fileBased().setFile(new File("master-url-config.xml")));
				
		return builder.getConfiguration();
	}
}