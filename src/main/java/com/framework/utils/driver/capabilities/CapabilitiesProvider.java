
package com.framework.utils.driver.capabilities;

import java.util.List;

import org.openqa.selenium.remote.DesiredCapabilities;

public interface CapabilitiesProvider {

	DesiredCapabilities createCapabilities(String browserUnderTest, String browserVersion,
			String operatingSystem, String mobileName, List<String> extensions);

	boolean canCreate(String browserUnderTest, String browserVersion, String operatingSystem, String mobileName);

}
