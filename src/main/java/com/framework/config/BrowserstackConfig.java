package com.framework.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.browserstack.local.Local;

@Configuration
public class BrowserstackConfig {
	
	private static final String BROWSER_STACK_HUB = "http://%s:%s@hub-cloud.browserstack.com/wd/hub";
		
	private Logger logger = LogManager.getLogger();
	
	@Value("${BROWSERSTACK_KEY}")
	private String key;
	
	@Value("${BROWSERSTACK_USER}")
	private String user;
	
	@Bean(destroyMethod = "stop")
	@Lazy(false)
	@ConditionalOnProperty(name = "BROWSER_STACK_TUNNEL")
	public Local browserStackLocal() throws Exception {
		Local browserStackTunnel = new Local();
		Map<String, String> options = new HashMap<>();
		options.put("key", key );
		options.put("-force-local", "true");
		browserStackTunnel.start(options);
		logger.warn("Browser stack tunnel started.");
		return browserStackTunnel;
	}
	
	@Bean("browserStackUrl")
	public String browserStackUrl() {
		 return String.format(BROWSER_STACK_HUB, user, key );
	}
}
