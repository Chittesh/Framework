package com.framework.utils.driver.capabilities;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import javax.inject.Inject;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;

import com.framework.utils.GridPlatformByOs;

public abstract class BaseChromeCapabilityProvider implements CapabilitiesProvider {

	@Inject
	protected GridPlatformByOs gridPlatformByOs;

	@Value("${CHROME_INCOGNITO_MODE:false}")
	private boolean chromeIncognitoMode;

	@Value("${DISABLE_CHROME_BROWSER_NOTIFICATIONS:true}")
	private boolean disableChromeBrowserNotification;

	@Override
	public DesiredCapabilities createCapabilities(String browserUnderTest, String browserVersion,
			String operatingSystem, String mobileName, List<String> extensions) {

		return new DesiredCapabilities();
	}

	protected abstract void modifiyOptions(ChromeOptions options, String mobileName, List<String> extensions);
}