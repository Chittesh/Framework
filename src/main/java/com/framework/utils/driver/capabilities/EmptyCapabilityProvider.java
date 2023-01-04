package com.framework.utils.driver.capabilities;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

@Component
public class EmptyCapabilityProvider extends BaseCapabilitityProvider implements CapabilitiesProvider {
	
	protected DesiredCapabilities createEmptySpecificCapabilityForBrowser(String browserUnderTest) {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability(CapabilityType.BROWSER_NAME, browserUnderTest);
		return caps;
	}

	@Override
	public boolean canCreate(String browserUnderTest, String browserVersion, String operatingSystem, String mobileName) {
		return false;
	}

}
