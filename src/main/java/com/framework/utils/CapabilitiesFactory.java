package com.framework.utils;

import static com.framework.utils.BrowserTypes.CHROME;
import static com.framework.utils.BrowserTypes.CHROMEMOBILEEMULATOR;
import static com.framework.utils.BrowserTypes.FIREFOX;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.framework.exception.AutomationException;
import com.framework.utils.driver.JsonDeviceDesiredCapabilitiesProducer;

public class CapabilitiesFactory {
	
	private TestEnvironmentExecutionContext context;

	private OptionsFactory optionsFactory;
	
	public CapabilitiesFactory(TestEnvironmentExecutionContext context) {
		this.context = context;
		this.optionsFactory = new OptionsFactory(context);
	}
	
	public DesiredCapabilities fromJson() {
		String dc = context.getDesiredCapabilitieId();
		
		if (dc.isEmpty()) 
			throw new AutomationException("empty capabilities");
		
		return new JsonDeviceDesiredCapabilitiesProducer("desiredCapability.json")
					.produce(dc)
					.orElseThrow(() -> new IllegalStateException("No desired capability found!"));
	}
	
	public DesiredCapabilities forChrome() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		ChromeOptions options = optionsFactory.forChrome();
		
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);		
		return capabilities;
	}
	
	public DesiredCapabilities forChromeInEmulationMode() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		ChromeOptions options = optionsFactory.forChromeInEmulationMode();
		
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);		
		return capabilities;
	}
	
	public DesiredCapabilities forFirefox() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		FirefoxOptions options = optionsFactory.forFirefox();		
		
		capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);		
		return capabilities;
	}
	
	public DesiredCapabilities forAppium(String appiumAppHash) {
		DesiredCapabilities caps = this.fromJson();
        caps.setCapability("app", appiumAppHash);
		return caps;
	}
	
	public DesiredCapabilities capabilitiesFor(String browser) {
    	CapabilitiesFactory capabilities = new CapabilitiesFactory(this.context);
		    	
		if (browser.equalsIgnoreCase(CHROME))				 	
			return capabilities.forChrome();
				
		if (browser.equalsIgnoreCase(CHROMEMOBILEEMULATOR))        		  	
			return capabilities.forChromeInEmulationMode();
				
		if (browser.equalsIgnoreCase(FIREFOX))
			return capabilities.forFirefox();
									
	    throw new AutomationException("invalid browser - " + browser);
	}
	
}