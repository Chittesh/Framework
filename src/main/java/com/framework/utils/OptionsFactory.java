package com.framework.utils;


import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.framework.exception.AutomationException;

import io.appium.java_client.android.options.UiAutomator2Options;

import static com.framework.utils.BrowserTypes.*;

public class OptionsFactory {
		
	private String browserVersion;
	private String operatingSystem;
	private String mobileName;

	public OptionsFactory(TestEnvironmentExecutionContext context) {
		this.browserVersion  = context.getBrowserVersion();
		this.operatingSystem = context.getOperatingSystem();
		this.mobileName      = context.getMobileName();
	}	
	
	public ChromeOptions forChrome() {
		
		ChromeOptions options = new ChromeOptions();
		
		if (isNotEmpty(this.browserVersion)) 
			options.setCapability(CapabilityType.BROWSER_VERSION, this.browserVersion);		
		
		options.setPlatformName(this.operatingSystem);

		options.addArguments("test-type");
		options.addArguments("disable-popup-blocking");
		options.addArguments("-incognito");
		options.addArguments("--disable-notifications");
		
		return options;
		
	}
	
	public ChromeOptions forHeadlessChrome() {
		
		ChromeOptions options = forChrome();
		options.setHeadless(true);

		return options;
		
	}
	
	public ChromeOptions forChromeInEmulationMode() {
		
		ChromeOptions options = forChrome();

		Map<String, String> mobileEmulation = new HashMap<>();
		mobileEmulation.put("deviceName", this.mobileName);
		options.setExperimentalOption("mobileEmulation", mobileEmulation);
		
		return options;
		
	}
	
	public FirefoxOptions forFirefox() {
		
		FirefoxOptions options = new FirefoxOptions();
		
		options.setPlatformName(this.operatingSystem);
		
		options.addArguments("-incognito");
		options.addArguments("--disable-notifications");
		
		return options;
		
	}
	
	public UiAutomator2Options uiAutomator2Options(String deviceName) {
		return new UiAutomator2Options().setDeviceName(deviceName)
			       						.eventTimings()
			       						.setAppWaitActivity("*")
			       						.setAppWaitForLaunch(true)
			       						.setAppWaitDuration(Duration.ofMillis(30000))			       
			       						.allowTestPackages()
			       						.autoGrantPermissions();
	}
	
	public AbstractDriverOptions<?> createOptionsFor(String browser) {
		if (browser.equalsIgnoreCase(CHROME))
			return forChrome();
		
		if (browser.equalsIgnoreCase(CHROMEMOBILEEMULATOR))
			return forChromeInEmulationMode();
		
		if (browser.equalsIgnoreCase(FIREFOX))
			return forFirefox();

		throw new AutomationException("invalid browser - " + browser);
	}

}
