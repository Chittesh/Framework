package com.framework.utils.driver.capabilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class ChromeMobileEmulatorProvider extends BaseChromeCapabilityProvider implements CapabilitiesProvider {
	
	@Override
	protected void modifiyOptions(ChromeOptions options, String mobileName, List<String> extensions) {
		Map<String, String> mobileEmulation = new HashMap<>();
		mobileEmulation.put("deviceName", mobileName);
		options.setExperimentalOption("mobileEmulation", mobileEmulation);
	}

	@Override
	public boolean canCreate(String browserUnderTest, String browserVersion, String operatingSystem, String mobileName) {
		return "chromemobileemulator".equalsIgnoreCase(browserUnderTest);
	}
}
