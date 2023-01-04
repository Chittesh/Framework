package com.framework.utils;

import static com.framework.utils.RunLocations.JENKINS_BROWSER_VERSION;
import static com.framework.utils.RunLocations.JENKINS_PARAMETER;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import org.springframework.beans.factory.annotation.Value;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.framework.utils.listeners.TestListener;

public class BaseTest {

	private Logger logger = LogManager.getLogger(StringFormatterMessageFactory.INSTANCE);

	private static final String POM_PROPERTIES_PATH = "target/my.properties";
	
	protected static final TestEnvironmentExecutionContext context = new TestEnvironmentExecutionContext();
	
	protected String mobileOSVersion = "";
	
    protected String deviceID = "";

    protected String appiumHash;
	
	@Value("${spring.profiles.active:default}")
	private String[] profiles;
	
	@BeforeClass(alwaysRun=true)
	public void injection() {
		TestListener.inject(this);
	}
	  
	@BeforeClass(alwaysRun=true)
	@Parameters({ "runLocation", 
				  "browserUnderTest", 
				  "browserVersion", 
				  "operatingSystem",  
				  "browserSize", 
				  "environment", 
				  "dc", 
				  "gridUrl" })
	public void setupBrowserEnvironment(@Optional("") String location, 
									    @Optional("") String browser, 
									    @Optional("") String version, 
									    @Optional("") String os, 
									    @Optional("") String size, 
									    String env, 
									    @Optional("") String dc, 
									    @Optional("") String gridUrl) { 	
		
		getContext().setEnvironment(env);
		
        setBrowserUnderTest(browser); // Use setter methods for these fields as data may be coming from jenkins parameter
        setBrowserVersion(version);
        setBrowserSize(size);
        setOperatingSystem(os);
        setRunLocation(location);
        
        getContext().setDesiredCapabilitieId(dc);
        
        getContext().setGridUrl(gridUrl);
	}

	@BeforeClass(alwaysRun=true)
	@Parameters({ "deviceName", 
				  "mobileName", 
				  "nativeApp", 
				  "description", 
				  "frameworkLogging", 
				  "percyStatus", 
				  "allowImplicitWait" })
	public void setupGeneralEnvironment(
			@Optional("") String deviceName, 
			@Optional("") String mobileName, 
			@Optional("") String nativeApp, 
			@Optional("") String description, 
			@Optional("true") String logging, 
			@Optional("false") String percy, 
			@Optional("true") String allowImplicitWait) { 	
		
		getContext().setDeviceName(deviceName);
		getContext().setNativeApp(nativeApp);
		
		getContext().setProfiles(profiles);
		getContext().setDescription(description); 				
		getContext().setFrameworkLogging(logging); 				
		getContext().setPercyStatus(percy); 				
		getContext().setAllowImplicitWait(allowImplicitWait);
		
	    getContext().setFrameworkVersion(PomInfo.getFrameworkVersion());
	    
	    String sonarSkipStatus = Boolean.TRUE.equals(new File(POM_PROPERTIES_PATH).exists())  
	    							? new PropertyInfo(POM_PROPERTIES_PATH).getSonarSkip() : "";
				
	    getContext().setSonarSkip(sonarSkipStatus);
		
        setMobileName(mobileName);		
	}
	
	@BeforeClass(alwaysRun=true)
	@Parameters({ "sonarScanExportToNewRelic"})
	public static void setupSonarScanExportToNewRelic(@Optional("true") String sonarScanExportToNewRelic) { 	
		getContext().setSonarScanExportToNewRelic(sonarScanExportToNewRelic);
	}

	@BeforeMethod(alwaysRun=true)
	public void setTestInfo(Method testMethod) { 	
		BaseTest.context.setTestClass(testMethod.getDeclaringClass());
	    BaseTest.context.setTestName(testMethod.getName());
	}
	
	@AfterMethod(alwaysRun=true)
	public void displayContextInfo(Method testMethod) { 	
		String contextInfo = context.toString();
		
		logger.debug("%1$s%n%n", contextInfo);
	}

	@AfterClass(alwaysRun=true)
	public void displayEnvironmentWarnings() { 	
		
		Boolean isSonarScanDisabled                   = toBoolean(context.getSonarSkip());
		Boolean isSonarMetricsExportToNewRelicEnabled = toBoolean(getContext().getSonarScanExportToNewRelic());
		
		if (Boolean.TRUE.equals(isSonarMetricsExportToNewRelicEnabled) && 
			Boolean.TRUE.equals(isSonarScanDisabled))
			
				logger.warn("\n\n-------------------------------------------------------------------------------------------------------------\n" +
							"   You have disabled the SONAR SCAN!\n" +
							"   SONAR SCAN is a recommended test automation practice that should stay enabled!\n" +
							"   Disabling it will not allow the export of the SONAR SCAN metrics to the New Relic management dashboards!\n" + 
						   	"-------------------------------------------------------------------------------------------------------------\n\n");
		
	}
	
	private Boolean toBoolean(String value) {
		return Boolean.parseBoolean(value);
	}
	
	//changed method to static
 	public static TestEnvironmentExecutionContext getContext() {
		return context;
	}
 	
 	@BeforeClass(alwaysRun=true)
    @Parameters({ "applicationUnderTest" })
    public void setApplicationUnderTest(@Optional("") String applicationUnderTest) {
		getContext().setApplicationUnderTest(applicationUnderTest);
    }

    public String getApplicationUnderTest() {	
    	return getContext().getApplicationUnderTest();	
    }

    protected void setPageURL(String url) 	{	
    	getContext().setPageUrl(url);					
    }
    
    public String getPageURL() 				{	
    	return getContext().getPageUrl();				
    }

    
    protected void setBrowserUnderTest(String but) {
    	String browserUnderTest = but.equalsIgnoreCase(JENKINS_PARAMETER) ? System.getProperty("jenkinsBrowser").trim() : but;
		getContext().setBrowserUnderTest(browserUnderTest);
    }

    public String getBrowserUnderTest() 	{
		return getContext().getBrowserUnderTest();
    }

    protected void setBrowserVersion(String bv) {
    	String version = bv;
    	
        if ( bv != null && bv.equalsIgnoreCase(JENKINS_PARAMETER)) {
        	String envVersion = System.getProperty(JENKINS_BROWSER_VERSION).trim();
        	version           = envVersion == null ? "" : envVersion;         				
        } 
        
        getContext().setBrowserVersion(version);
    }

    public String getBrowserVersion() 		{	
    	return getContext().getBrowserVersion();	
    }

    public void setMobileName(String mn) 	{	
    	getContext().setMobileName(mn);				
    }
    
    public String getMobileName() 			{	
    	return getContext().getMobileName();		
    }
    
    protected void setMobileOSVersion(String mobileOSVersion) {
        this.mobileOSVersion = mobileOSVersion;
    }
    
    protected void setBrowserSize(String bs) {
    	String size = bs.equalsIgnoreCase(JENKINS_PARAMETER) ? System.getProperty("jenkinsBrowserSize").trim() : bs;
		getContext().setBrowserSize(size);
    }

    public String getBrowserSize() {
		return getContext().getBrowserSize();
    }

    protected void setOperatingSystem(String os) {
        String operatingSystem = os.equalsIgnoreCase(JENKINS_PARAMETER) ? System.getProperty("jenkinsOperatingSystem").trim() : os;
		getContext().setOperatingSystem(operatingSystem);
    }

    public String getOperatingSystem() {
		return getContext().getOperatingSystem();
    }

    protected void setRunLocation(String rl) {
        String location = rl.equalsIgnoreCase(JENKINS_PARAMETER) ? System.getProperty("jenkinsRunLocation").trim() : rl;
        getContext().setRunLocation(location);
    }

    public String getRunLocation() {	
    	return getContext().getRunLocation();		
    }    
    
    protected void setRoleName(String role) 		{	
    	getContext().setUserRole(role);				
    }
    
    public String getRoleName() 					{	
    	return getContext().getUserRole();			
    }

    protected void setTestEnvironment(String env) 	{	
    	getContext().setEnvironment(env);			
    }
    
    public String getTestEnvironment() 				{	
    	return getContext().getEnvironment();		
    }

    protected void setTestName(String tn) 			{	
    	getContext().setTestName(tn);				
    }
    
    public String getTestName() 					{	
    	return getContext().getTestName();			
    }
    
    @BeforeMethod(alwaysRun = true)
   	public void makeContextAvailable(ITestContext context) {
   		context.setAttribute("testEnvironmentContext", getContext());
   	}
    
    public String getFrameworkLogging() 	{	
    	return getContext().getFrameworkLogging();	
    }
    
    public String getPercyStatus() 			{	
    	return getContext().getPercyStatus();		
    }

	@BeforeClass(alwaysRun=true)
	@Parameters( "appiumApplicationHash" )
	public void setAppiumApplicationHash(@Optional("") String appiumApplicationHash) {
		this.appiumHash = appiumApplicationHash;
		getContext().setAppiumAppHash(appiumApplicationHash);
	}
	
	public String getAppiumApplicationHash() {
		return this.appiumHash;
	}
	
    @BeforeClass(alwaysRun=true)
	@Parameters( "deviceId" )
	public void setDeviceID(@Optional("") String deviceId) {
	    this.deviceID = deviceId;
	    getContext().setDeviceId(deviceId);
	}
    
    public String getDeviceID() 					{	return this.deviceID;	    				}

    public void setDeviceName(String deviceName) 	{	getContext().setDeviceName(deviceName);	    }
    public String getDeviceName() 					{	return getContext().getDeviceName();		}

    public void setNativeApp(String nativeApp) 		{	getContext().setNativeApp(nativeApp);		}
    public String getNativeApp() 					{	return getContext().getNativeApp();			}

}
