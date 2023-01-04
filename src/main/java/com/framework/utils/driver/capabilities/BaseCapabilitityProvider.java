package com.framework.utils.driver.capabilities;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import javax.inject.Inject;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.framework.utils.GridPlatformByOs;

public abstract class BaseCapabilitityProvider implements CapabilitiesProvider {

	@Inject
	private GridPlatformByOs gridPlatformByOs;

	public BaseCapabilitityProvider() {
		super();
	}

	@Override
	public DesiredCapabilities createCapabilities(String browserUnderTest, String browserVersion, String operatingSystem, String mobileName, List<String> extensions) {
		DesiredCapabilities caps = createEmptySpecificCapabilityForBrowser(browserUnderTest);
		
		if (isNotEmpty(browserVersion)) {
			caps.setCapability(CapabilityType.VERSION, browserVersion);
		}
		caps.setPlatform(gridPlatformByOs.getGridPlatformByOS(operatingSystem));
		return caps;
	}

	protected abstract DesiredCapabilities createEmptySpecificCapabilityForBrowser(String browserUnderTest);

}