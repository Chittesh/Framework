package com.framework.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RemoteUrlProvider {

	private static final String BROWSERSTACK_RUN_LOCATION = "browserstack";	
	private static final String MOBILE = "mobile";
	
	private Map<String, String> remoteUrls = new HashMap<>();
	
    @Inject @Named("browserStackUrl")
    private String browserStackUrl;
    
    @Value("${VDI_SELENIUM_GRID_HUB_URL}")
	private String vdiSeleniumHubURL;
    
    @Value("${DOCKER_SELENIUM_GRID_HUB_URL}")
	private String dockerSeleniumHubURL;
    
    @Value("${MOBILE_HUB_URL}")
	private String mobileHubURL;   
    
	@PostConstruct
	private void postInjection() {
    	remoteUrls.put("grid", vdiSeleniumHubURL);
    	remoteUrls.put("docker", dockerSeleniumHubURL != null ? dockerSeleniumHubURL : Constants.DOCKER_HUB);
    	remoteUrls.put(BROWSERSTACK_RUN_LOCATION, browserStackUrl);
    	remoteUrls.put("appium", browserStackUrl);
    	remoteUrls.put(MOBILE, mobileHubURL);
	}
	
    public String getRemoteURL(String runLocation) {
    	
    	return remoteUrls.entrySet().stream()
    		.filter(entry -> matchesRunLocation(entry, runLocation))
    		.map( Map.Entry::getValue)
    		.findAny()
    		.orElse("");
    }
    
    public URL getURL(String runLocation) throws MalformedURLException {
    	return new URL(getRemoteURL(runLocation));
    }
    
	private boolean matchesRunLocation(Map.Entry<String, String> entry, String runLocation) {
		return entry.getKey().equalsIgnoreCase(runLocation);
	}
}
