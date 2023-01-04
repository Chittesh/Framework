package com.framework.utils.driver.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

@Component
public class FirefoxCapabilitiesProvider extends BaseCapabilitityProvider implements CapabilitiesProvider {
	
	@Override
	public boolean canCreate(String browserUnderTest, String browserVersion, String operatingSystem, String mobileName) {
		return "firefox".equalsIgnoreCase(browserUnderTest);
	}

	@Override
	protected DesiredCapabilities createEmptySpecificCapabilityForBrowser(String browserUnderTest) {
		 return new DesiredCapabilities();
	}

}
