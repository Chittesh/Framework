package com.framework.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TestEnvironmentExecutionContext
{

	private String applicationUnderTest = "";
	private String appiumAppHash = "";
	private String deviceId;
	private String deviceName;
	private String nativeApp;
	private String browserUnderTest = "";
	private String browserVersion = "";
	private String browserSize = "";
	private String operatingSystem = "";
	private String runLocation = "";
	private String description = ""; 		
	private String frameworkLogging = ""; 		
	private String percyStatus = "";
	private String environment = "";
	private String mobileName = "";
	private String testName = "";
	private String pageUrl = "";
	private String userRole = "";
	private String desiredCapabilitieId = "";
	private String frameworkVersion = "";
	private String allowImplicitWait = "";
	private String[] profiles = new String[] {};
	private Class<?> testClass;
	private String gridUrl;

	private String sonarSkip = "";
	private String sonarScanExportToNewRelic = "";
	
	public String getSonarScanExportToNewRelic() {
		return sonarScanExportToNewRelic;
	}

	public void setSonarScanExportToNewRelic(String sonarScanExportToNewRelic) {
		this.sonarScanExportToNewRelic = sonarScanExportToNewRelic;
	}

	public String getSonarSkip() {
		return sonarSkip;
	}

	public void setSonarSkip(String sonarSkip) {
		this.sonarSkip = sonarSkip;
	}

	public String getApplicationUnderTest() {
		return applicationUnderTest;
	}

	public void setApplicationUnderTest(String applicationUnderTest) {
		this.applicationUnderTest = applicationUnderTest;
	}

	public String getBrowserUnderTest() {
		return browserUnderTest;
	}

	public void setBrowserUnderTest(String browserUnderTest) {
		this.browserUnderTest = browserUnderTest;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getBrowserSize() {
		return browserSize;
	}

	public void setBrowserSize(String browserSize) {
		this.browserSize = browserSize;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getRunLocation() {
		return runLocation;
	}

	public void setRunLocation(String runLocation) {
		this.runLocation = runLocation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFrameworkLogging() {
		return frameworkLogging;
	}

	public void setFrameworkLogging(String frameworkLogging) {
		this.frameworkLogging = frameworkLogging;
	}

	public String getPercyStatus() {
		return percyStatus;
	}

	public void setPercyStatus(String percyStatus) {
		this.percyStatus = percyStatus;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getMobileName() {
		return mobileName;
	}

	public void setMobileName(String mobileName) {
		this.mobileName = mobileName;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getDesiredCapabilitieId() {
		return desiredCapabilitieId;
	}

	public void setDesiredCapabilitieId(String desiredCapabilitieId) {
		this.desiredCapabilitieId = desiredCapabilitieId;
	}

	public String getFrameworkVersion() {
		return frameworkVersion;
	}

	public void setFrameworkVersion(String frameworkVersion) {
		this.frameworkVersion = frameworkVersion;
	}

	public String getAllowImplicitWait() {
		return allowImplicitWait;
	}

	public void setAllowImplicitWait(String allowImplicitWait) {
		this.allowImplicitWait = allowImplicitWait;
	}

	public String[] getProfiles() {
		return profiles;
	}

	public void setProfiles(String[] profiles) {
		this.profiles = profiles;
	}

	public Class<?> getTestClass() {
		return testClass;
	}

	public void setTestClass(Class<?> testClass) {
		this.testClass = testClass;
	}	
	
	public String getAppiumAppHash() {
		return appiumAppHash;
	}

	public void setAppiumAppHash(String appiumAppHash) {
		this.appiumAppHash = appiumAppHash;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getNativeApp() {
		return nativeApp;
	}

	public void setNativeApp(String nativeApp) {
		this.nativeApp = nativeApp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(profiles);
		result = prime * result + Objects.hash(allowImplicitWait, applicationUnderTest, browserSize, browserUnderTest,
				browserVersion, description, desiredCapabilitieId, environment, frameworkLogging, frameworkVersion,
				mobileName, operatingSystem, pageUrl, percyStatus, runLocation, testName, userRole);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		TestEnvironmentExecutionContext other = (TestEnvironmentExecutionContext) obj;
		
		return Objects.equals(allowImplicitWait, other.allowImplicitWait)
				&& Objects.equals(applicationUnderTest, other.applicationUnderTest)
				&& Objects.equals(browserSize, other.browserSize)
				&& Objects.equals(browserUnderTest, other.browserUnderTest)
				&& Objects.equals(browserVersion, other.browserVersion)
				&& Objects.equals(description, other.description)
				&& Objects.equals(desiredCapabilitieId, other.desiredCapabilitieId)
				&& Objects.equals(environment, other.environment)
				&& Objects.equals(frameworkLogging, other.frameworkLogging)
				&& Objects.equals(frameworkVersion, other.frameworkVersion)
				&& Objects.equals(mobileName, other.mobileName)
				&& Objects.equals(operatingSystem, other.operatingSystem) && Objects.equals(pageUrl, other.pageUrl)
				&& Objects.equals(percyStatus, other.percyStatus) && Arrays.equals(profiles, other.profiles)
				&& Objects.equals(runLocation, other.runLocation)
				&& Objects.equals(testName, other.testName) && Objects.equals(userRole, other.userRole);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(" ----- Test Environment Execution Context ----- ");
		
		Map<String, String> map = new HashMap<>();
		
		map.put("allowImplicitWait"   , allowImplicitWait);
		map.put("appiumAppHashId"     , appiumAppHash);
		map.put("applicationUnderTest", applicationUnderTest);
		map.put("browserSize"         , browserSize);
		map.put("browserUnderTest"    , browserUnderTest);
		map.put("browserVersion"      , browserVersion);
		map.put("description"         , description);
		map.put("desiredCapabilitieId", desiredCapabilitieId);
		map.put("deviceName"		  , deviceName);
		map.put("environment"		  , environment);
		map.put("frameworkLogging"    , frameworkLogging);
		map.put("frameworkVersion"	  , frameworkVersion);
		map.put("mobileName"		  , mobileName);
		map.put("nativeApp"			  , nativeApp);
		map.put("operatingSystem"     , operatingSystem);
		map.put("pageUrl"			  , pageUrl);
		map.put("percyStatus"         , percyStatus);
		map.put("runLocation"         , runLocation);
		map.put("testName"			  , testName);
		map.put("userRole"			  , userRole);
		map.put("gridUrl"			  , gridUrl);
		
		map.put("sonarSkip"		      		, sonarSkip);
		map.put("sonarScanExportToNewRelic" , sonarScanExportToNewRelic);
		
		TreeMap<String, String> sortedMap = new TreeMap<>(map);
		
		for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
			String value = entry.getValue();
			
			if (Boolean.FALSE.equals(value.isEmpty()))
				
				stringBuilder.append(
							String.format("%n  - %s = %s", 
										  entry.getKey(), 
										  entry.getValue()));
			
		}
		
		return stringBuilder.toString();
	}

	public String getGridUrl() {
		return gridUrl;
	}

	public void setGridUrl(String gridUrl) {
		this.gridUrl = gridUrl;
	}
	
}
